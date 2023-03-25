using System.Collections.Generic;
using System.Reflection;
using System.Text;
using System;

using Jsonzai.Attributes;
using Jsonzai.Converters;
using Jsonzai.Cache.Setters;

namespace Jsonzai.Cache {

    class KlassInfo {

        public Type KlassType { get; private set; }

        /********* Cache ***********/

        // Properties cache
        public Dictionary<string, PropertyInfo> properties = new Dictionary<string, PropertyInfo>();
        // setters -> <Property Name, Setter>
        public Dictionary<string, BaseSetter> setters = new Dictionary<string, BaseSetter>();
        // converters -> <Property Name, Converter>
        public Dictionary<string, IConverter> converters = new Dictionary<string, IConverter>();

        /**************************/

        public KlassInfo(Type type) {
            this.KlassType = type;
            InitProps(KlassType.GetProperties());
        }

        public virtual void InitProps(PropertyInfo[] props) {
            foreach (PropertyInfo p in props) {
                string name = GetPropName(p);
                properties.Add(name, p);
            }
        }

        public string GetPropName(PropertyInfo p) {
            string name = p.Name;
            if (Utils.TryGetAttribute(p, typeof(JsonPropertyAttribute), out Attribute attr))
                name = ((JsonPropertyAttribute)attr).Name;
            return name;
        }

        public BaseSetter GetSetter(string key) {

            // If setter hasn't been created yet or there is a new converter
            // for that setter property -> Build new Setter
            if (!setters.TryGetValue(key, out BaseSetter setter)) {
                setter = BuildSetter(key);
                setters.Add(key, setter);
            }

            if (converters.ContainsKey(key)) {
                setters[key] = BuildSetter(key);
            }

            return setters[key];
        }

        public virtual BaseSetter BuildSetter(string pname) {
            PropertyInfo prop = properties[pname];

            // Converters added by delegate
            if(converters.TryGetValue(pname, out IConverter converter)) {
                converters.Remove(pname);
                return new SetterWithConverter(prop, converter);
            }
            // Converters added by Custom Atributte
            if (Utils.TryGetAttribute(prop, typeof(JsonConvertAttribute), out Attribute attr))
                return new SetterWithConverter(prop, (IConverter)Activator.CreateInstance(((JsonConvertAttribute)attr).ConverterType));

            return new Setter(prop);
        }

        // No more properties to initialize.
        // Check if there are still any converters to be applied
        // Let the user know that the given property names
        // don't match any property of the given class
        private void ResolveRemainingConverters()
        {
            if (converters.Count != 0)
            {
                StringBuilder error = new StringBuilder("The following Property names don't match any Property in the given Class(" + KlassType.Name + "):\n");
                foreach (string pname in converters.Keys)
                {
                    error.Append(pname).Append("\n");
                }
                converters.Clear();
                Console.WriteLine(error);
            }
        }
    }
}
