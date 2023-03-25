using Isel.Ave14.SqlMapper;
using SqlMapperNorthwind.Model;
using System;
using System.Data.SqlClient;

namespace SqlMapperNorthwind.Mapper
{
    class ProductDataMapper:AbstractDataMapper<Product>
    {
        private ISqlExecutorFactory fac;

        public ProductDataMapper(ISqlExecutorFactory fac)
            : base(fac)
        {
            this.fac = fac;
        }
        protected override string StrGetAll()
        {
            return "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products";
        }

        protected override bool Convert(SqlDataReader reader, out Product next)
        {
            if (reader.Read())
            {
                next = (Product) Activator.CreateInstance(typeof(Product),
                    reader["ProductID"], 
                    reader["ProductName"], 
                    reader["UnitPrice"], 
                    reader["UnitsInStock"]);
                return true;
            }
            else
            {
                next = null;
                return false;
            }

        }
    }
}
