using SqlReflect.Attributes;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Reflection;

namespace SqlReflect
{
    public class ReflectDataMapper<K, V> : AbstractDataMapper<K,V>
    {
        readonly string getAllStmt;
        readonly string getByIdStmt;
        readonly string insertStmt;
        readonly string deleteStmt;
        readonly string updateStmt;
        readonly List<IPropertyMapper> props;
        readonly Type klass;
        IPropertyMapper pk;

        public ReflectDataMapper(Type klass, string connStr, bool withCache) : base(connStr, withCache)
        {
            this.klass = klass;
            TableAttribute table = klass.GetCustomAttribute<TableAttribute>();
            if (table == null) throw new InvalidOperationException(klass.Name + " should be annotated with Table custom attribute !!!!");
            props = klass
                .GetProperties()
                .Select(p => GetColumnForProperty(p, connStr, withCache))
                .ToList();
            pk = props.First(p => p.IsPk);
            string columns = String.Join(",", props.Where(p => !p.IsPk).Select(p => p.Column));
            getAllStmt = "SELECT " + pk.Column + "," + columns + " FROM " + table.Name;
            getByIdStmt = getAllStmt + " WHERE " + pk.Column + "=";
            insertStmt = "INSERT INTO " + table.Name + "(" + columns + ") OUTPUT INSERTED." + pk.Column + " VALUES ";
            deleteStmt = "DELETE FROM " + table.Name + " WHERE " + pk.Column + "=";
            updateStmt = "UPDATE " + table.Name + " SET {0} WHERE " + pk.Column + "={1}";
        }

        private static IPropertyMapper GetColumnForProperty(PropertyInfo prop, string connStr, bool withCache)
        {
            Type k = prop.PropertyType;
            if (k.IsPrimitive || k == typeof(string) || k.IsArray)
                return new PropertyMapper(prop);
            return new PropertyMapperFk(prop, connStr, withCache);
        }

        protected override string SqlGetAll()
        {
            return getAllStmt;
        }

        protected override string SqlGetById(object id)
        {
            return getByIdStmt + id;
        }

        protected override string SqlInsert(object target)
        {
            IEnumerable<string> values = props
                            .Where(p => !p.IsPk)
                            .Select(p => "'" + p.Get(target) + "'");
            return insertStmt + "(" + String.Join(",", values) + ")";
        }

        protected override string SqlUpdate(object target)
        {
            IEnumerable<string> values = props
                            .Where(p => !p.IsPk)
                            .Select(p => p.Column + "='"+ p.Get(target) + "'");
            return string.Format(updateStmt, String.Join(",", values), pk.Get(target));
        }

        protected override string SqlDelete(object target)
        {
            return deleteStmt + pk.Get(target);
        }
        protected override object Load(IDataReader dr)
        {
            return props.Aggregate(
                Activator.CreateInstance(klass),
                (target, p) => p.Set(dr, target));
        }
    }
}
