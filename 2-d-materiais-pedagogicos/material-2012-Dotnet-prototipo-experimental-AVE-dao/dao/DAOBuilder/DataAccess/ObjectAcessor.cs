using System;
using System.Reflection;

namespace DAOBuilder.DataAccess
{
    internal class ObjectAcessor:AbstractAccessor<object>
    {
        public override object CreateObject(string[] names, object[] values, Type type)
        {

            object ret = null;

            for (int i = 0; i < values.Length; ++i)
            {
                FieldInfo fi1;
                if ((fi1 = type.GetField(names[i])) != null && fi1.FieldType.IsAssignableFrom(values[i].GetType()))
                {
                    if (ret == null)
                        ret = Activator.CreateInstance(type);
                    fi1.SetValue(ret, values[i]);
                }
                else
                {
                    PropertyInfo pi1;
                    if ((pi1 = type.GetProperty(names[i])) != null && pi1.CanWrite && pi1.PropertyType.IsAssignableFrom(values[i].GetType()))
                    {
                        if (ret == null)
                            ret = Activator.CreateInstance(type);

                        pi1.SetValue(ret, values[i]);
                    }
                }
            }

            if (ret == null)
                return type.IsValueType ? Activator.CreateInstance(type) : null;
            return ret;
        }
    }
}
