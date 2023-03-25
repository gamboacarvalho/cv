using SqlReflect.Attributes;
using System;
using System.Reflection;
using System.Data.SqlClient;
using System.Data;

namespace SqlReflect
{
    class PropertyMapper : IPropertyMapper
    {
        readonly PropertyInfo prop;

        public PropertyMapper(PropertyInfo prop)
        {
            this.prop = prop;
        }

        public string Column => prop.Name;

        public bool IsPk => prop.IsDefined(typeof(PKAttribute));

        public object Get(object target)
        {
            return prop.GetValue(target);
        }

        public object Set(IDataReader dr, object target)
        {
            object val = dr[prop.Name];
            val = val is DBNull ? null : val;
            prop.SetValue(target, val);
            return target;
        }
    }
}
