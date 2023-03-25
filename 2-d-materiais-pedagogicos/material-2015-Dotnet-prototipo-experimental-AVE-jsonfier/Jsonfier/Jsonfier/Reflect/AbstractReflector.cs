using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Jsonzai.Reflect
{
    public abstract class AbstractReflector : Reflector
    {
        public Dictionary<string, object> ValuesFrom(object src)
        {
            return src.GetType().IsArray ?
                ValuesFromArray((IEnumerable)src)
                : ValuesFromObject(src);
        }

        protected abstract Dictionary<string, object> ValuesFromObject(object src);

        protected Dictionary<string, object> ValuesFromArray(IEnumerable src)
        {
            Dictionary<string, object> res = new Dictionary<string, object>();
            int i = 0;
            foreach (var item in src)
            {
                res.Add(i.ToString(), item);
                i++;
            }
            return res;
        }
    }
}
