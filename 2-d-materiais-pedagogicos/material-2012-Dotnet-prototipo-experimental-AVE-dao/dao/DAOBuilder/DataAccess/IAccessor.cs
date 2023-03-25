using System;
using System.Collections.Generic;

namespace DAOBuilder.DataAccess
{
    internal interface IAccessor<out T>
    {
        T DoQuerySingle(string connStr, string cmd, Type returnType, IEnumerable<KeyValuePair<string, object>> args);
        IEnumerable<T> DoQueryMultiple(string connStr, string cmd, Type returnType, IEnumerable<KeyValuePair<string, object>> args);
        void DoNonQuery(string connStr, string cmd, IEnumerable<KeyValuePair<string, object>> args);
        object DoScalar(string connStr, string cmd, Type returnType, IEnumerable<KeyValuePair<string, object>> args);
        IEnumerable<object> DoScalarMultiple(string connStr, string cmd, Type returnType,IEnumerable<KeyValuePair<string, object>> args);
    }
}
