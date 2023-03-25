using Isel.Ave14.Reflector;
using Isel.Ave14.SqlMapper;
using SqlMapperNorthwind.Mapper;
using SqlMapperNorthwind.Model;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;

namespace SqlMapperNorthwind
{
    class Program
    {
        static void Main(string[] args)
        {
            /*
            object[] ctorArgs = { 1, "ole", ((decimal) 34.5), ((short) 7)};
            Product p = (Product)Activator.CreateInstance(typeof(Product), ctorArgs);
            Console.WriteLine(p);
            */


            ISqlExecutorFactory fac = 
                new SqlExecutorSingleCallFactory(
                    SetupConnnectionStr());

            /*
            ProductDataMapper mapper = new ProductDataMapper(fac);
            foreach (Product p in mapper.GetAll())
            {
                Console.WriteLine(p);
            }
            */

            Builder b = new Builder(fac);
            IDataMapper<Product> prodMapper = b.Build<Product>();
            IEnumerable<Product> prods = prodMapper.GetAll();

            foreach (Product p in prodMapper .GetAll())
            {
                Console.WriteLine(p);
            }
        }

        public static string SetupConnnectionStr()
        {
            SqlConnectionStringBuilder builder = new SqlConnectionStringBuilder();
            builder.DataSource = @"DRAGAO\SQLEXPRESS";
            builder.IntegratedSecurity = true;
            builder.InitialCatalog = "Northwind";
            return builder.ConnectionString;
        }
    }
}
