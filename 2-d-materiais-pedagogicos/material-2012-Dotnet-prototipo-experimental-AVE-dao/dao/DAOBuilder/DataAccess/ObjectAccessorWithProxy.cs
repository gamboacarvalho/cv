using System;
using System.Collections.Generic;
using LinFu.DynamicProxy;

namespace DAOBuilder.DataAccess
{
    internal sealed class ObjectAccessorWithProxy : AbstractAccessor<object>
    {
        private readonly ProxyFactory _factory;

        public ObjectAccessorWithProxy(ProxyFactory factory)
        {
            _factory = factory;
        }

        public override object CreateObject(string[] names, object[] values, Type type)
        {

            var fields = new Dictionary<string, object>();

            for (int i = 0; i < values.Length; ++i)
            {
                fields.Add(names[i], values[i]);
            }

            return _factory.CreateProxy(type, new ObjectInterceptor(fields));
        }

    }
}
