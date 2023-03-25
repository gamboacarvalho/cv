using System.Reflection;

namespace FireMapper
{
    public class PropertySimple : AbstractProperty
    {
        private readonly PropertyInfo prop;

        public PropertySimple(PropertyInfo field) : base(field.Name)
        {
            this.prop = field;
        }

        public override object GetValue(object target) => prop.GetValue(target);

        public override void SetValue(object target, object val) => prop.SetValue(target, val);
    }
}