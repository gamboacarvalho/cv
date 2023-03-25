using System.Data;
using System.Data.SqlClient;

namespace DAOTest
{
    internal static class ProductCommandBuilder
    {
        public static SqlCommand MakeRead(SqlConnection conSql)
        {
            SqlCommand cmd = conSql.CreateCommand();
            cmd.CommandText = "SELECT * FROM Products WHERE ProductID = @id";

            SqlParameter id = new SqlParameter("@id", SqlDbType.Int);
            cmd.Parameters.Add(id);

            return cmd;
        }

        public static SqlCommand MakeReadAll(SqlConnection conSql)
        {
            SqlCommand cmd = conSql.CreateCommand();
            cmd.CommandText = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products";

            return cmd;
        }



        public static SqlCommand MakeInsert(SqlConnection conSql)
        {
            string strUpdate = "INSERT INTO Products (ProductName, UnitPrice, UnitsInStock) OUTPUT INSERTED.ProductID VALUES (@name, @price, @stock)";

            SqlCommand cmd = conSql.CreateCommand();

            cmd.Parameters.Add(new SqlParameter("@name", SqlDbType.NVarChar));
            cmd.Parameters.Add(new SqlParameter("@price", SqlDbType.Money));
            cmd.Parameters.Add(new SqlParameter("@stock", SqlDbType.SmallInt));

            cmd.CommandText = strUpdate;

            return cmd;
        }

        public static SqlCommand MakeUpdate(SqlConnection conSql)
        {
            string strUpdate = "UPDATE Products SET ProductName = @name, UnitPrice = @price, UnitsInStock = @stock WHERE ProductID = @id";

            SqlCommand cmd = conSql.CreateCommand();

            cmd.Parameters.Add(new SqlParameter("@name", SqlDbType.NVarChar));
            cmd.Parameters.Add(new SqlParameter("@price", SqlDbType.Money));
            cmd.Parameters.Add(new SqlParameter("@stock", SqlDbType.SmallInt));
            cmd.Parameters.Add(new SqlParameter("@id", SqlDbType.Int));

            cmd.CommandText = strUpdate;

            return cmd;
        }
    }
}
