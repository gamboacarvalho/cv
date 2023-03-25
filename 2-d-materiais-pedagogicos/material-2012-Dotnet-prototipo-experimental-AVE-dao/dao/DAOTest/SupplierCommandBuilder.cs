using System.Data;
using System.Data.SqlClient;

namespace DAOTest
{
    internal static class SupplierCommandBuilder
    {

            public static SqlCommand MakeRead(SqlConnection conSql)
            {
            SqlCommand cmd = conSql.CreateCommand();
            cmd.CommandText = "SELECT *  FROM Suppliers WHERE SupplierID = @id";

            SqlParameter id = new SqlParameter("@id", SqlDbType.Int);
            cmd.Parameters.Add(id);

            return cmd;
            }

        public static SqlCommand MakeReadAll(SqlConnection conSql)
        {
            SqlCommand cmd = conSql.CreateCommand();
            cmd.CommandText = "SELECT SupplierID, CompanyName, Address, Country  FROM Suppliers";

            return cmd;
        }

        public static SqlCommand MakeReadAllCountries(SqlConnection conSql)
        {
            SqlCommand cmd = conSql.CreateCommand();
            cmd.CommandText = "SELECT distinct Country FROM Suppliers";
            return cmd;
        }



        public static SqlCommand MakeInsert(SqlConnection conSql)
        {
            string strUpdate = "INSERT INTO Suppliers (CompanyName, ContactName, ContactTitle, Address, City, Country) OUTPUT INSERTED.SupplierID VALUES (@companyName, @contactName, @title,@address,@city,@country)";

            SqlCommand cmd = conSql.CreateCommand();

            cmd.Parameters.Add(new SqlParameter("@companyName", SqlDbType.NVarChar));
            cmd.Parameters.Add(new SqlParameter("@contactName", SqlDbType.NVarChar));
            cmd.Parameters.Add(new SqlParameter("@title", SqlDbType.NVarChar));
            cmd.Parameters.Add(new SqlParameter("@address", SqlDbType.NVarChar));
            cmd.Parameters.Add(new SqlParameter("@city", SqlDbType.NVarChar));
            cmd.Parameters.Add(new SqlParameter("@country", SqlDbType.NVarChar));

            cmd.CommandText = strUpdate;

            return cmd;
        }

        public static SqlCommand MakeUpdate(SqlConnection conSql)
        {
            string strUpdate = "UPDATE Suppliers SET ContactName = @name, ContactTitle = @title WHERE SupplierID = @id";

            SqlCommand cmd = conSql.CreateCommand();

            cmd.Parameters.Add(new SqlParameter("@name", SqlDbType.NVarChar));
            cmd.Parameters.Add(new SqlParameter("@title", SqlDbType.NVarChar));
            cmd.Parameters.Add(new SqlParameter("@id", SqlDbType.Int));

            cmd.CommandText = strUpdate;

            return cmd;
        }
    }
}
