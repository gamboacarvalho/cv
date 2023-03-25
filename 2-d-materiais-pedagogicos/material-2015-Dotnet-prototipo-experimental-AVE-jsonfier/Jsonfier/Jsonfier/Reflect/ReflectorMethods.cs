using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;

namespace Jsonzai.Reflect
{
    public class ReflectorMethods : AbstractReflector
    {
        protected override Dictionary<string, object> ValuesFromObject(object src)
        {
            return src
                .GetType()
                .GetMethods(BindingFlags.Public | BindingFlags.Instance | BindingFlags.DeclaredOnly)
                .Where(m => m.ReturnType != typeof(void) && m.GetParameters().Length == 0)
                .Aggregate(
                    new Dictionary<string, object>(),
                    (dic, f) =>
                    {
                        dic.Add(f.Name, f.Invoke(src, new object[0]));
                        return dic;
                    });
        }

    }
}
