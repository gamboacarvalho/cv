using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;
using System.Data.Common;
using System.Data.SqlClient;

namespace SqlReflect
{
    public abstract class AbstractDataMapper<K,V> : IDataMapper<K,V>
    {
        readonly string connStr;
        readonly DataSet cache;

        public AbstractDataMapper(string connStr, bool withCache)
        {
            this.connStr = connStr;
            if (withCache)
                cache = new DataSet();
        }

        protected abstract string SqlGetById(object id);
        protected abstract string SqlGetAll();
        protected abstract string SqlInsert(object target);
        protected abstract string SqlUpdate(object target);
        protected abstract string SqlDelete(object target);

        protected abstract object Load(IDataReader dr);

        public V GetById(K id)
        {
            string sql = SqlGetById(id);
            if (cache != null) { 
                string tableName = GetTableNameFromSql(sql, "FROM ");
                DataTable table = cache.Tables[tableName];
                if (table == null) GetAll();
            }
            IEnumerator<V> iter = Get(sql).GetEnumerator();
            return iter.MoveNext() ? iter.Current : default(V);
        }


        public IEnumerable<V> GetAll()
        {
            return Get(SqlGetAll());
        }
        private IEnumerable<V> Get(string sql)
        {
            string tableName = GetTableNameFromSql(sql, "FROM ");
            if(cache == null)
                return GetFromDb(sql, tableName);

            DataTable table = cache.Tables[tableName];
            return table != null
                ? DataReaderToList(sql, table.CreateDataReader())
                : GetFromDb(sql, tableName);
        }

        private IEnumerable<V> GetFromDb(string sql, string tableName)
        {
            using (SqlConnection con = new SqlConnection(connStr))
            {
                using (SqlCommand cmd = con.CreateCommand())
                {
                    cmd.CommandText = sql;
                    con.Open();
                    DbDataReader dr = cmd.ExecuteReader();
                    dr = AddToCache(dr, tableName);
                    return DataReaderToList(dr);
                }
            }
        }

        private IEnumerable<V> DataReaderToList(IDataReader dr)
        {
            while (dr.Read()) yield return (V) Load(dr);
        }

        private IEnumerable<V> DataReaderToList(string sql, IDataReader dr)
        {
            string[] clause = sql
                .ToUpper()
                .Split(new[] { " WHERE " }, StringSplitOptions.None)
                [1]  // Last part
                .Split('=');
            while (dr.Read())
            {
                if (clause != null)
                {
                    string col = clause[0].Trim();
                    string val = clause[1].Trim();
                    if (!dr[col].ToString().Equals(val)) continue;
                }
                yield return (V) Load(dr);
            }
        }

        public K Insert(V target)
        {
            string sql = SqlInsert(target);
            string tableName = GetTableNameFromSql(sql, "INTO ");
            return (K) Execute(sql, tableName);
        }

        public void Delete(V target)
        {
            string sql = SqlDelete(target);
            string tableName = GetTableNameFromSql(sql, "FROM ");
            Execute(sql, tableName);
        }


        public void Update(V target)
        {
            string sql = SqlUpdate(target);
            string tableName = GetTableNameFromSql(sql, "UPDATE ");
            Execute(sql, tableName);
        }

        private object Execute(string sql, string tableName)
        {
            RemoveFromCache(tableName);
            SqlConnection con = new SqlConnection(connStr);
            SqlCommand cmd = null;
            try
            {
                cmd = con.CreateCommand();
                cmd.CommandText = sql;
                con.Open();
                return cmd.ExecuteScalar();
            }
            finally
            {
                if (cmd != null) cmd.Dispose();
                if (con.State != ConnectionState.Closed) con.Close();
            }
        }

        private DbDataReader AddToCache(DbDataReader dr, string tableName)
        {
            if (cache == null)
                return dr;
            cache.Tables.Add(tableName).Load(dr);
            return cache.Tables[tableName].CreateDataReader();
        }

        private void RemoveFromCache(string tableName)
        {
            if (cache != null && cache.Tables.Contains(tableName))
                cache.Tables.Remove(tableName);
        }

        private static string GetTableNameFromSql(string sql, string word)
        {
            return sql
                .ToUpper()
                .Split(new[] { word }, StringSplitOptions.None)
                [1]  // Last part
                .Split(' ')
                [0]; // First word
        }

        object IDataMapper.GetById(object id)
        {
            return this.GetById((K) id);
        }

        IEnumerable IDataMapper.GetAll()
        {
            return this.GetAll();
        }

        object IDataMapper.Insert(object target)
        {
            return this.Insert((V)target);
        }

        void IDataMapper.Update(object target)
        {
            this.Update((V)target);
        }

        void IDataMapper.Delete(object target)
        {
            this.Delete((V)target);
        }
    }
}
