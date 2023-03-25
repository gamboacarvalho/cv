using Jsonzai.Attributes;
using Jsonzai.Converters;
using Jsonzai.Cache.Setters;
using System;
using System.Reflection;

namespace Jsonzai.Cache {

    class KlassInfoEmit : KlassInfo {

        private SetterGenerator Generator { get; set; }
        public KlassInfoEmit(Type type) : base(type) {
            Generator = new SetterGenerator(type);
        }

        public override BaseSetter BuildSetter(string pname)
        {
            PropertyInfo prop = properties[pname];

            // Converters added by delegate
            if (converters.TryGetValue(pname, out IConverter converter)) {
                converters.Remove(pname);
                return Generator.GenerateSetter(prop, converter);
            }
            // Converters added by Custom Atributte
            if (Utils.TryGetAttribute(prop, typeof(JsonConvertAttribute), out Attribute attr))
                return Generator.GenerateSetter(prop, (IConverter)Activator.CreateInstance(((JsonConvertAttribute)attr).ConverterType));

            return Generator.GenerateSetter(prop, null); ;
        }
    }
}
