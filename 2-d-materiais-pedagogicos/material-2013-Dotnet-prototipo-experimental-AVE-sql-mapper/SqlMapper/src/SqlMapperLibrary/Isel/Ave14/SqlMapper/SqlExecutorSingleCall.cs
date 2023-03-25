using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Isel.Ave14.SqlMapper
{
    public class SqlExecutorSingleCall : ISqlExecutor
    {
        private SqlConnection conn;

        public SqlExecutorSingleCall(string connStr)
        {
            conn = new SqlConnection(connStr);
        }

        public SqlDataReader ExecuteReader(string cmdStr)
        {
                using (SqlCommand cmd = conn.CreateCommand())
                {
                    cmd.CommandText = cmdStr;
                    conn.Open();
                    return cmd.ExecuteReader();
                }
        }

        public void Dispose()
        {
            if (conn.State != ConnectionState.Closed)
            {
                conn.Dispose();
                conn = null;
            }
        }
    }
}
