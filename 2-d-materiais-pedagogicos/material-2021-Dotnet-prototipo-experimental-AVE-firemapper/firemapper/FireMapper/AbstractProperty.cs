namespace FireMapper
{
    public abstract class AbstractProperty : IProperty
    {
        private readonly string name;

        protected AbstractProperty(string name)
        {
            this.name = name;
        }

        public string GetName() => name;
        public abstract object GetValue(object target);
        public abstract void SetValue(object target, object val);
    }
}