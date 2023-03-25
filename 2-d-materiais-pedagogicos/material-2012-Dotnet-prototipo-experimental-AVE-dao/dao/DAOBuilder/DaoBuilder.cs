using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using DAOBuilder.Attributes;
using DAOBuilder.DataAccess;
using LinFu.DynamicProxy;

namespace DAOBuilder
{
    public static class DaoBuilder
    {

        private static readonly ProxyFactory Factory = new ProxyFactory();

        public static TDao Build<TDao,T>(string connStr) where T:ISettable, new()
        {
            return Build<TDao, T>(connStr, new GenericAccessor<T>(), true);
        }

        public static T Build<T>(string connStr, bool proxy = false)
        {
            return Build<T, object>(connStr, proxy?(IAccessor<object>) new ObjectAccessorWithProxy(Factory) : new ObjectAcessor(), false);
        }

        private static TDao Build<TDao, T>(string connStr,IAccessor<T> accessor,bool isGenericEnumerable)
        {
            Type type = typeof(TDao);

            var dmc = new DaoMethodCollection(connStr);

            foreach (MethodInfo mi in type.GetMethods())
            {
                var attr = (SqlCmdAttribute)Attribute.GetCustomAttribute(mi, typeof(SqlCmdAttribute));

                Type returnType = mi.ReturnType;
                if (attr == null) continue;


                string cmd = attr.Command;

                var names = mi.GetParameters().Select(par => new KeyValuePair<string, Type>(par.Name, par.ParameterType));


                if (returnType == typeof(void))
                {
                    dmc.AddMethodVoid(mi.Name, cmd, names, accessor.DoNonQuery);
                }
                else if (returnType.IsGenericType &&
                         returnType.GetGenericTypeDefinition() == typeof(IEnumerable<>) )
                {
                    Type t = returnType.GetGenericArguments()[0];
                    var isValueType = t.IsPrimitive || t == typeof(DateTime) || t == typeof(string);
                    dmc.AddMethodIEnumerable(mi.Name, cmd, names, returnType, isValueType, isGenericEnumerable,accessor.DoScalarMultiple);
                }
                else if (returnType.IsPrimitive || returnType == typeof(DateTime) || mi.ReturnType == typeof(string))
                {
                    dmc.AddMethodOther(mi.Name, cmd, names, mi.ReturnType, accessor.DoScalar);
                }
                else if (!returnType.IsAbstract && !returnType.IsArray && returnType.IsClass || returnType.IsValueType)
                {
                    dmc.AddMethodOther(mi.Name, cmd, names, returnType, accessor.DoQuerySingle);
                }
            }
            return CreateDao<TDao, T>(dmc);
        }



        private static TDao CreateDao<TDao,T>(DaoMethodCollection dmc)
        {
            return Factory.CreateProxy<TDao>(new DaoInterceptor<T>(dmc));
        }


    }
}
