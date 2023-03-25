namespace FireMapper
{

    public interface IProperty 
    {
        string GetName();

        object GetValue(object target);

        void SetValue(object target, object val);
    }
}