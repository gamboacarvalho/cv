using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using FireMapper.Attributes;
using FireSource;

namespace FireMapper
{
    public record FireDataMapper(Type Entity, string ProjectId, string CredentialsPath) : IDataMapper
    {
        readonly List<IProperty> props = Entity
                                                .GetProperties()
                                                .Where(p => !p.IsDefined(typeof(FireIgnoreAttribute)))
                                                .Select(p =>
                                                {
                                                    return p.PropertyType.IsPrimitive || p.PropertyType == typeof(String)
                                                        ? new PropertySimple(p) as IProperty
                                                        : new PropertyComplex(p, ProjectId, CredentialsPath) as IProperty;
                                                })
                                                .ToList();
        readonly IDataSource source = InitDataSource(Entity, ProjectId, CredentialsPath);

        private static IDataSource InitDataSource(Type entity, string projectId, string credentialsPath)
        {
            /// 
            /// Search for property with FireKey annotation
            /// 
            List<PropertyInfo> keys = entity.GetProperties().Where(p => p.IsDefined(typeof(FireKeyAttribute))).ToList();
            if(keys.Count == 0) throw new ArgumentException($"Type {entity.Name} missing a property annotated with [FireKey]!");
            if(keys.Count > 1) throw new ArgumentException($"Type {entity.Name} has more than on property with [FireKey]!");
            /// 
            /// Get the collection Name.
            /// 
            FireCollectionAttribute attr = entity.GetCustomAttribute<FireCollectionAttribute>();
            if(attr == null) throw new ArgumentException($"Type {entity.Name} missing the FireStore collection name in [FireCollection] annotation!");
            /// 
            /// Create new FireDataSource instance
            /// 
            return new FireDataSource(projectId, attr.Collection, keys[0].Name, credentialsPath);
        }

        public void Add(object obj)
        {
            Dictionary<string, object> dic = new Dictionary<string, object>();
            props.ForEach(p => dic.Add(p.GetName(), p.GetValue(obj)));
            source.Add(dic);
        }

        public void Delete(object keyValue)
        {
            source.Delete(keyValue);
        }

        public IEnumerable GetAll() => source
                .GetAll()
                .Select(dic =>
                {
                    object target = Activator.CreateInstance(Entity);
                    props.ForEach(p => p.SetValue(target, dic[p.GetName()]));
                    return target;
                });

        public object GetById(object keyValue)
        {
            Dictionary<string, object> dic = source.GetById(keyValue);
            if(dic == null) return null;
            object target = Activator.CreateInstance(Entity);
            props.ForEach(p => p.SetValue(target, dic[p.GetName()]));
            return target;
        }

        public void Update(object obj)
        {
            Dictionary<string, object> dic = new Dictionary<string, object>();
            props.ForEach(p => dic.Add(p.GetName(), p.GetValue(obj)));
            source.Update(dic);
        }
    }
}