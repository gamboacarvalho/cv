using System.Collections.Generic;
using System.Linq;
using LinFu.DynamicProxy;

namespace DAOBuilder
{
    internal class DaoInterceptor<T>:IInterceptor
    {
        private readonly DaoMethodCollection _collection;
        private MethodKey _methodKey;

        
        public DaoInterceptor(DaoMethodCollection col)
        {
            _collection = col;
        }

        public object Intercept(InvocationInfo info)
        {

            _methodKey.Name = info.TargetMethod.Name;
            _methodKey.ReturnType = info.TargetMethod.ReturnType;
            _methodKey.ParamTypes = info.TargetMethod.GetParameters().Select(pi => pi.ParameterType).ToArray();


            var method = _collection[_methodKey];

            var args = method.ParamNames.Zip(info.Arguments, (n, a) => new KeyValuePair<string, object>(n, a));
            return method.DoAccess(args);


        }
    }
}
