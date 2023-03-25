using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;

namespace DAOBuilder.DataAccess
{
    internal abstract class AbstractAccessor<T>:IAccessor<T>
    {
        public abstract T CreateObject(string[] names, object[] values, Type type);
        public T DoQuerySingle(string connStr, string cmd, Type returnType, IEnumerable<KeyValuePair<string, object>> args)
        {
            using (SqlConnection conSql = new SqlConnection(connStr))
            {
                using (SqlCommand com = conSql.CreateCommand())
                {
                    com.CommandText = cmd;

                    foreach (var pair in args)
                    {
                        com.Parameters.AddWithValue("@" + pair.Key, pair.Value);
                    }

                    conSql.Open();
                    SqlDataReader dr = com.ExecuteReader();

                    if (!dr.Read())
                        return default(T);
                    object[] values = new object[dr.FieldCount];

                    dr.GetValues(values);
                    return CreateObject(Enumerable.Range(0, dr.FieldCount).Select(dr.GetName).ToArray(),values,returnType);

                }
            }
        }

        public IEnumerable<T> DoQueryMultiple(string connStr, string cmd, Type returnType, IEnumerable<KeyValuePair<string, object>> args)
        {
            using (SqlConnection conSql = new SqlConnection(connStr))
            {
                using (SqlCommand com = conSql.CreateCommand())
                {
                    com.CommandText = cmd;

                    foreach (var pair in args)
                    {
                        com.Parameters.AddWithValue("@" + pair.Key, pair.Value);
                    }

                    conSql.Open();
                    SqlDataReader dr = com.ExecuteReader();

                    while (dr.Read())
                    {

                        object[] values = new object[dr.FieldCount];
                        dr.GetValues(values);

                        yield return CreateObject(Enumerable.Range(0, dr.FieldCount).Select(dr.GetName).ToArray(), values,returnType);

                    }

                }
            }
        }

        public void DoNonQuery(string connStr, string cmd, IEnumerable<KeyValuePair<string, object>> args)
        {
            using (SqlConnection conSql = new SqlConnection(connStr))
            {
                using (SqlCommand com = conSql.CreateCommand())
                {
                    com.CommandText = cmd;

                    foreach (var pair in args)
                    {
                        com.Parameters.AddWithValue("@" + pair.Key, pair.Value);
                    }

                    conSql.Open();
                    com.ExecuteNonQuery();

                }
            }
        }

        public object DoScalar(string connStr, string cmd, Type returnType, IEnumerable<KeyValuePair<string, object>> args)
        {
            using (SqlConnection conSql = new SqlConnection(connStr))
            {
                using (SqlCommand com = conSql.CreateCommand())
                {
                    com.CommandText = cmd;

                    foreach (var pair in args)
                    {
                        com.Parameters.AddWithValue("@" + pair.Key, pair.Value);
                    }

                    conSql.Open();
                    object ret = com.ExecuteScalar();

                    if (ret==null||!ret.GetType().IsAssignableFrom(returnType))
                        return Activator.CreateInstance(returnType);
                    return ret;
                }
            }
        }

        public IEnumerable<object> DoScalarMultiple(string connStr, string cmd, Type returnType,
            IEnumerable<KeyValuePair<string, object>> args)
        {
            using (SqlConnection conSql = new SqlConnection(connStr))
            {
                using (SqlCommand com = conSql.CreateCommand())
                {
                    com.CommandText = cmd;

                    foreach (var pair in args)
                    {
                        com.Parameters.AddWithValue("@" + pair.Key, pair.Value);
                    }

                    conSql.Open();
                    SqlDataReader dr = com.ExecuteReader();

                    while (dr.Read())
                    {

                        object[] values = new object[dr.FieldCount];
                        dr.GetValues(values);

                        yield return values[0];

                    }

                }
            }
        }
    }
}
