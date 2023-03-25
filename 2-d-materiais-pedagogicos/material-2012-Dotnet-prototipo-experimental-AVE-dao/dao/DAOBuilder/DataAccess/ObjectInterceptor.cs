using System;
using System.Collections.Generic;
using LinFu.DynamicProxy;

namespace DAOBuilder.DataAccess
{
    internal class ObjectInterceptor : IInterceptor
    {
        private Dictionary<string, object> _fields;
        public ObjectInterceptor(Dictionary<string, object> fields)
        {
            _fields = fields;
        }

        public object Intercept(InvocationInfo info)
        {
            var tMethod = info.TargetMethod;
            if (!tMethod.IsSpecialName)
                return tMethod.ReturnType.IsValueType ? Activator.CreateInstance(tMethod.ReturnType) : null;
            if (tMethod.Name.Contains("get_") && _fields.ContainsKey(tMethod.Name.Substring(4)))
            {
                
                return _fields[tMethod.Name.Substring(4)];
            }
                    
            if (tMethod.Name.Contains("set_"))
            {
                if (_fields.ContainsKey(tMethod.Name.Substring(4)))
                    _fields[tMethod.Name.Substring(4)] = info.Arguments[0];
                else
                {
                    _fields.Add(tMethod.Name.Substring(4),info.Arguments[0]);
                }
            }

            return tMethod.ReturnType.IsValueType?Activator.CreateInstance(tMethod.ReturnType):null;
        }
    }
}