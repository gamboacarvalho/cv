using System.Data;
using System.Data.SqlClient;

namespace DAOTest
{
    internal static class CategoryCommandBuilder
    {
        public static SqlCommand MakeReadAll(SqlConnection conSql)
        {
            SqlCommand cmd = conSql.CreateCommand();
            cmd.CommandText = "SELECT CategoryID, CategoryName, Description FROM Categories";

            return cmd;
        }

        public static SqlCommand MakeInsert(SqlConnection conSql)
        {
            string strUpdate = "INSERT into Categories(CategoryName,Description) OUTPUT INSERTED.CategoryID values(@name,@desc)";

            SqlCommand cmd = conSql.CreateCommand();

            cmd.Parameters.Add(new SqlParameter("@name", SqlDbType.NVarChar));
            cmd.Parameters.Add(new SqlParameter("@desc", SqlDbType.NText));

            cmd.CommandText = strUpdate;

            return cmd;
        }
    }
}
