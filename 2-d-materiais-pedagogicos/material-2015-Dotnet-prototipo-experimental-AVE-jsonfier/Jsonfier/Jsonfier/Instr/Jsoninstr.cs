using Jsonzai.Reflect;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;

namespace Jsonzai.Instr
{
    public class Jsoninstr
    {
        private static readonly Dictionary<Type, Serializer> jsons;

        static Jsoninstr()
        {
            jsons = new Dictionary<Type, Serializer>();
        }
        public static string ToJson(object src)
        {
            Type srcKlass = src.GetType();
            if (srcKlass == null || srcKlass == typeof(String) || srcKlass.IsPrimitive || srcKlass.IsArray)
                return Jsonfier.ToJson(src);
            Serializer js;
            jsons.TryGetValue(srcKlass, out js);
            if(js == null) {
                js = SerializerBuilder.BuildSerializer(srcKlass);
                jsons.Add(src.GetType(), js);
            }
            return js.ToJson(src);
        }
    }
}
