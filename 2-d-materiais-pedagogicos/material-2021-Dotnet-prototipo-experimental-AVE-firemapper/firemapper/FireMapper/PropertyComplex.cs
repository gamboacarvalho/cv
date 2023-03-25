using System;
using System.Linq;
using System.Reflection;
using FireMapper.Attributes;

namespace FireMapper
{
    public class PropertyComplex : AbstractProperty
    {
        private readonly PropertyInfo prop;
        private readonly IDataMapper mapper;
        private readonly PropertyInfo propKey;

        public PropertyComplex(PropertyInfo prop, string projectId, string credentialsPath) : base(prop.Name)
        {
            this.prop = prop;
            this.mapper = new FireDataMapper(prop.PropertyType, projectId, credentialsPath);
            this.propKey = prop
                .PropertyType
                .GetProperties()
                .Where(p => p.IsDefined(typeof(FireKeyAttribute)))
                .FirstOrDefault();
            if(propKey == null) throw new InvalidOperationException("Missing FireKey attribute on " + prop.PropertyType);
        }

        /*
         * This is used on Add and Update to get the value that we will put on Diccionary.
         * In that case we must get the key of the object hold in the property.
         */
        public override object GetValue(object target)
        {
            object subObject = prop.GetValue(target); // the object hold in the property.
            return propKey.GetValue(subObject);       // the key on the subObject.
        }
        /*
         * This is used on GetAll and GetBy to put a value put on a property.
         * In that case we must get the sub object from the DataMapper that has the given id.
         */
        public override void SetValue(object target, object id)
        {
            object subObject = mapper.GetById(id); // object from the DataMapper that has the given id.
            prop.SetValue(target, subObject);      // put that object on target.
        }
    }
}