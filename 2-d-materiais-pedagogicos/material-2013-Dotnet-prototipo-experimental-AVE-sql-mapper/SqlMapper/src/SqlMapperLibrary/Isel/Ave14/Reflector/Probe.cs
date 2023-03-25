using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace Isel.Ave14.Reflector
{
    public class Probe
    {
        public static IEnumerable<String> PropertiesNames<T>()
        {
            PropertyInfo[] props = typeof(T).GetProperties();
            foreach (PropertyInfo p in props)
            {
                yield return p.Name;
            }
        }

    }
}
