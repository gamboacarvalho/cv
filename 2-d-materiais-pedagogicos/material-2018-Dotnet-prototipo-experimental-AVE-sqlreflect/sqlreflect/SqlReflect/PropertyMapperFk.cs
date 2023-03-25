using System;
using System.Linq;
using System.Reflection;
using System.Data.SqlClient;
using SqlReflect.Attributes;
using System.Data;

namespace SqlReflect
{
    class PropertyMapperFk: IPropertyMapper
    {
        readonly PropertyInfo prop;
        readonly PropertyInfo fkPk;
        readonly string col;
        readonly IDataMapper fkMapper;
        public PropertyMapperFk(PropertyInfo prop, string connStr, bool withCache)
        {
            this.prop = prop;
            fkPk = prop
                    .PropertyType
                    .GetProperties()
                    .FirstOrDefault(p => p.IsDefined(typeof(PKAttribute), false));
            if (fkPk == null)
                throw new InvalidOperationException("Missing PK annotation on type " + prop.PropertyType);
            col = fkPk.Name;
            Type[] typeArgs = { fkPk.PropertyType, prop.PropertyType };
            Type reflectMapperType = typeof(ReflectDataMapper<,>).MakeGenericType(typeArgs);
            fkMapper = (IDataMapper) Activator.CreateInstance(
                reflectMapperType,
                prop.PropertyType, connStr, withCache);
        }

        public string Column => col;

        public bool IsPk => false;

        public object Get(object target)
        {
            object fk = prop.GetValue(target);
            return fkPk.GetValue(fk);
        }

        public object Set(IDataReader dr, object target)
        {
            object key = dr[Column];
            if (key != null)
                prop.SetValue(target, fkMapper.GetById(key));
            return target;
        }
    }
}
