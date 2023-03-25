using System;

namespace DAOBuilder.DataAccess
{
    internal sealed class GenericAccessor<T> : AbstractAccessor<T> where T : ISettable, new()
    {
        public override T CreateObject(string[] names, object[] values, Type type)
        {

            T t = new T();

            for (int i = 0; i < values.Length; ++i)
            {
                t.Set(names[i], values[i]);
            }

            return t;
        }
    }
}
