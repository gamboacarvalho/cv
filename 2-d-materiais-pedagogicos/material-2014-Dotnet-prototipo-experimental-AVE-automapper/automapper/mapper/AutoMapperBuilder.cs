using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text;

namespace Mapper
{
    public class AutoMapperBuilder<TSrc, TDest>
    {

        Dictionary<PropertyInfo, Action<TSrc, TDest>> mappers = new Dictionary<PropertyInfo, Action<TSrc, TDest>>();


        public AutoMapperBuilder<TSrc, TDest> IgnoreMember(string propDestName) 
        {
            return this;
        }
        public AutoMapperBuilder<TSrc, TDest> IgnoreMember<TAnot>() where TAnot : Attribute
        {
            return this;
        }
        public AutoMapperBuilder<TSrc, TDest> ForMember<TPropDest>(string propDestName, Func<TSrc, TPropDest> resolver)
        {
            PropertyInfo pDest = typeof(TDest).GetProperty(propDestName);
            if(pDest.PropertyType != typeof(TPropDest))
                throw new InvalidOperationException("TPropDest does not match type of the porperty " + propDestName);
            mappers.Add(pDest, (src, dest) => pDest.SetValue(dest, resolver(src)));
            return this;
        }



        public Mapper<TSrc, TDest> CreateMapper()
        {
            List<Action<TSrc, TDest>> res = new List<Action<TSrc, TDest>>();
            typeof(TDest).GetProperties().Aggregate(res, (acc, prop) => {
                Action<TSrc, TDest> m;
                if (mappers.TryGetValue(prop, out m))
                    acc.Add(m);
                else { 
                    PropertyInfo propSrc = typeof(TSrc).GetProperty(prop.Name);
                    if(propSrc != null && propSrc.PropertyType == prop.PropertyType)
                    acc.Add((src, dest) => prop.SetValue(dest, propSrc.GetValue(src)));
                }
                return acc; 
            });
            return new Mapper<TSrc, TDest>(res.ToArray());
        }
    }
}
