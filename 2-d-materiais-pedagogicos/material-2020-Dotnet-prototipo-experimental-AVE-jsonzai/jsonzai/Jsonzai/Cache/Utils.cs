using System;
using System.Reflection;

namespace Jsonzai.Cache {
    public class Utils {
        public static bool TryGetAttribute(PropertyInfo p, Type attrType, out Attribute attr) {
            object[] attrs = p.GetCustomAttributes(attrType, true);
            if (attrs.Length != 0) {
                attr = (Attribute)attrs[0];
                return true;
            }
            attr = null;
            return false;
        }
    }
}
