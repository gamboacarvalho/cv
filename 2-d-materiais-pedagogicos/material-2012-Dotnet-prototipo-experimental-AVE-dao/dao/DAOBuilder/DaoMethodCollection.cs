using System;
using System.Collections.Generic;
using System.Linq;

namespace DAOBuilder
{
    internal delegate object AccessorCall(IEnumerable<KeyValuePair<string, object>> args);

    internal class DaoMethodCollection
    {
        public string ConnectionString { get; set; }
        private readonly Dictionary<MethodKey,Method> _methods = new Dictionary<MethodKey, Method>();

        public Method this[MethodKey mk ]
        {
            get
            {
                try
                {
                    return _methods[mk];
                }
                catch
                    (KeyNotFoundException)
                {
                    throw new NotImplementedException(string.Format("The method {0} is not implemented", mk ));
                }
            } 
        }

        public DaoMethodCollection(string connStr)
        {
            ConnectionString = connStr;
        }

        public void AddMethodOther<T>(string name, string cmd, IEnumerable<KeyValuePair<string, Type>> paramsList, Type returnType, Func<string, string, Type, IEnumerable<KeyValuePair<string, object>>, T> func)
        {
            AddMethodInternal(name, paramsList, returnType, args => func(ConnectionString, cmd, returnType, args));
        }

        public void AddMethodIEnumerable<T>(string name, string cmd, IEnumerable<KeyValuePair<string, Type>> paramsList, Type returnType, bool isScalar, bool isGeneric, Func<string, string, Type, IEnumerable<KeyValuePair<string, object>>, IEnumerable<T>> func)
        {
            AccessorCall accessorCall;
            if (isGeneric&&isScalar)
                accessorCall =
                    args => { throw new NotImplementedException("Build<TDao,T> only supports IEnumerable<T>"); };
            else
            {
                accessorCall = args =>
                {
                    var t = func(ConnectionString, cmd, returnType.GenericTypeArguments[0], args);
                    if (isGeneric) return t;
                    Type oiType = typeof (ObjectIEnumerable<>).MakeGenericType(returnType.GenericTypeArguments);

                    return Activator.CreateInstance(oiType, new object[] {t});

                };
            }
          
            AddMethodInternal(name, paramsList, returnType, accessorCall);
        }

        public void AddMethodVoid(string name, string cmd, IEnumerable<KeyValuePair<string, Type>> paramsList, Action<string, string, IEnumerable<KeyValuePair<string, object>>> func)
        {
            AddMethodInternal(name, paramsList, typeof(void),args => { func(ConnectionString, cmd, args); return null; });
        }

        private void AddMethodInternal(string name, IEnumerable<KeyValuePair<string, Type>> paramsList, Type returnType, AccessorCall func)
        {
            var keyValuePairs = paramsList.ToList(); //para evitar múltiplas iterações
            _methods.Add(new MethodKey
            {
                Name = name,
                ReturnType = returnType,
                ParamTypes = keyValuePairs.Select(kv => kv.Value).ToArray()
            },
                new Method(func, keyValuePairs.Select(kv => kv.Key)));
        }
    }
}
