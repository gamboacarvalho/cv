using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data.SqlClient;
using System.Collections;

namespace Isel.Ave14.SqlMapper
{
    public abstract class AbstractDataMapper<T> : IDataMapper<T> where T : class
    {
        private readonly ISqlExecutorFactory execFac;
        private readonly string[] pkNames;

        public AbstractDataMapper(ISqlExecutorFactory fac)
        {
            this.execFac = fac;
            pkNames = typeof(T).GetProperties()
                .Where(p => p.IsDefined(typeof(SqlPkAttribute), true))
                .Select(p => p.Name)
                .ToArray();
        }


        public ISqlEnumerable<T> GetAll()
        {

            return new SqlEnumerable<T>(
                    execFac,
                    StrGetAll(),
                    new SqlConverterHandler<T>(Convert)
                );
        }

        public void Update(T val)
        {
            throw new NotImplementedException();
        }

        public void Delete(T val)
        {
            throw new NotImplementedException();
        }

        public void Insert(T val)
        {
            throw new NotImplementedException();
        }

        protected abstract string StrGetAll();

        protected abstract bool Convert(SqlDataReader reader, out T next);



        ISqlEnumerable IDataMapper.GetAll()
        {
            return GetAll();
        }

        void IDataMapper.Update(object val)
        {
            Update((T) val);
        }

        void IDataMapper.Delete(object val)
        {
            Delete((T)val);
        }

        void IDataMapper.Insert(object val)
        {
            Insert((T)val);
        }


        public string[] PkNames
        {
            get 
            {
                return pkNames;
            }
        }

    }
}
