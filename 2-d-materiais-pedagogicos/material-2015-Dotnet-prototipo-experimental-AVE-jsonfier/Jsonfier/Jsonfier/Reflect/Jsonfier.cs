using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Jsonzai.Reflect
{
    public class Jsonfier
    {
        public static string ToJson(object src) {
            return ToJson(src, new ReflectorProps());
        }

        public static string ToJson(object src, Reflector reflector)
        {
            if (src == null) return "null";
            Type klass = src.GetType();
            if (klass == typeof(String)) return "\"" + src + "\"";
            if (klass.IsPrimitive) return src.ToString().ToLower();
            if (klass.IsArray) return new NodeArray(reflector, src).Stringify();
            else return new NodeObject(src, reflector).Stringify();
        }
    }
}
