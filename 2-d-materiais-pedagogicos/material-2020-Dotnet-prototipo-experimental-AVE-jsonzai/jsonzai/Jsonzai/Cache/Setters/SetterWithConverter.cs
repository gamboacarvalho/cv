using Jsonzai.Converters;
using System.Reflection;

namespace Jsonzai.Cache.Setters {

    public class SetterWithConverter : BaseSetter {

        public SetterWithConverter(PropertyInfo propInfo, IConverter converter) : base(propInfo, converter) { }

        public override object SetValue(object target, object val) {
            PropInfo.SetValue(target, Converter.Convert((string)val));
            return target;
        }
    }
}
