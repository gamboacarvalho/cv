using Isel.Ave14.Reflector;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Reflection;
using System.Text;

namespace Isel.Ave14.SqlMapper
{
    class AutoDataMapper<T> : AbstractDataMapper<T> where T : class
    {
        private readonly ISqlExecutorFactory fac;
        private readonly string[] keysNames;
        private readonly Builder builder;
        private readonly Dictionary<String, IDataMapper> foreignMappers; // Maps property name to corresponding mapper
        private readonly string strGetAll;

        public AutoDataMapper(Builder builder, ISqlExecutorFactory fac, params string[] keysNames)
            : base(fac)
        {
            this.builder = builder;
            this.fac = fac;
            this.keysNames = keysNames;
            this.foreignMappers = new Dictionary<string, IDataMapper>();
            this.strGetAll = InitSrGetAll();
        }

        private string InitSrGetAll()
        {
            List<string> cols = new List<string>();
            foreach (PropertyInfo p in typeof(T).GetProperties())
            {
                if (!(typeof(IEnumerable).IsAssignableFrom(p.PropertyType)) || p.PropertyType == typeof(string))
                {
                    cols.Add(p.Name);
                }
                else 
                {
                    Type typeArg = p.PropertyType.GetGenericArguments()[0];
                    MethodInfo mBuild = typeof(Builder).GetMethod("Build");
                    mBuild = mBuild.MakeGenericMethod(typeArg);
                    IDataMapper mapper = (IDataMapper)mBuild.Invoke(builder, null);
                    foreignMappers[p.Name] = mapper;
                }
            }
            return String.Format("SELECT {0} FROM {1}", 
                cols.Aggregate((cur, next) => cur + ", " + next), 
                typeof(T).Name + "s");
        }

        protected override string StrGetAll()
        {
            return strGetAll;
        }

        protected override bool Convert(SqlDataReader reader, out T next)
        {
            if (reader.Read())
            {
                T obj = Activator.CreateInstance<T>();
                typeof(T)
                    .GetProperties()
                    .ToList()
                    .ForEach(p => UpdateProperty(obj, p, reader));
                next = obj;
                return true;
            }
            else
            {
                next = null;
                return false;
            }

        }

        private void UpdateProperty(object target, PropertyInfo p, SqlDataReader reader) 
        {
            Object res;
            if (!foreignMappers.ContainsKey(p.Name))
            {
                res = reader[p.Name];
                if (res is DBNull) res = res.ToString(); // !!! quick hammer
            }
            else
            {
                IDataMapper foreignMapper = foreignMappers[p.Name];
                String clause = String.Join(
                    " AND ", 
                    this.PkNames.Select(pkName => pkName + " = " + reader[pkName]));
                res = foreignMapper.GetAll().Where(clause);
            }
            p.SetValue(target, res);
        }

    }
}
