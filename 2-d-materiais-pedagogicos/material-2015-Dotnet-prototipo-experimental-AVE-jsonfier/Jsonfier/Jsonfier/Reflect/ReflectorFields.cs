using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;

namespace Jsonzai.Reflect
{
    public class ReflectorFields : AbstractReflector
    {
        
        protected override Dictionary<string, object> ValuesFromObject(object src)
        {
            return src
                .GetType()
                .GetFields(BindingFlags.NonPublic | BindingFlags.Public | BindingFlags.Instance)
                .Aggregate(
                    new Dictionary<string, object>(),
                    (dic, f) =>
                    {
                        dic.Add(f.Name, f.GetValue(src));
                        return dic;
                    });
        }

    }
}
