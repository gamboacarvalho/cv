using System.Reflection;

namespace Jsonzai.Cache.Setters {

    public class Setter : BaseSetter {

        public Setter(PropertyInfo propInfo) : base(propInfo) { }

        public override object SetValue(object target, object val) {
            PropInfo.SetValue(target, val);
            return target;
        }
    }
}
