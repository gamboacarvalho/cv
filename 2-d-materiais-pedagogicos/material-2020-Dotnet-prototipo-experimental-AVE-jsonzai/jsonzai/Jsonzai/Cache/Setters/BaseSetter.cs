using Jsonzai.Converters;
using System.Reflection;

namespace Jsonzai.Cache.Setters {

    public abstract class BaseSetter {
        public PropertyInfo PropInfo { get; set; }
        public IConverter Converter { get; set; }

        public BaseSetter(PropertyInfo propInfo, IConverter converter) {
            PropInfo = propInfo;
            Converter = converter;
        }

        public BaseSetter(PropertyInfo propInfo) {
            PropInfo = propInfo;
        }

        public abstract object SetValue(object target, object val);
    }
}
