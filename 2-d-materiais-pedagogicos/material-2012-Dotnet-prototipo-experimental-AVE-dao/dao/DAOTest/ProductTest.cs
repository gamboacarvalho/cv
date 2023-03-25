using System;
using System.Collections;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Transactions;
using DAOBuilder;
using DAOModel;
using DAOModel.Interfaces;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace DAOTest
{
    [TestClass]
    public class ProductTest
    {
        private string _connectionString;
        private SqlConnection _conn;

        [TestInitialize]
        public void Setup()
        {
            _connectionString = ConfigurationManager.ConnectionStrings["Northwind"].ConnectionString;

            _conn = new SqlConnection(_connectionString);
        }

        [TestCleanup]
        public void TearDown()
        {
            if (_conn.State != ConnectionState.Closed)
                _conn.Close();
            _conn.Dispose();
        }

        [TestMethod]
        public void GetByIdTest()
        {
            string name = "PRODUCTO DE TESTE";
            decimal price = 12345.6789m;
            short stock = 100;

            int id;
            using (var ts = new TransactionScope())
            {
                _conn.Open();
                using (SqlCommand insertCommand = ProductCommandBuilder.MakeInsert(_conn))
                {
                    insertCommand.Parameters["@name"].Value = name;
                    insertCommand.Parameters["@price"].Value = price;
                    insertCommand.Parameters["@stock"].Value = stock;

                    id = (int)insertCommand.ExecuteScalar();
                }
                _conn.Close();


                IProductDao dao = DaoBuilder.Build<IProductDao>(_connectionString, true);

                Product prod = dao.GetById(id);

                Assert.AreEqual(id, prod.ProductID);
                Assert.AreEqual(name,prod.ProductName);
                Assert.AreEqual(stock,prod.UnitsInStock);
                Assert.AreEqual(price,prod.UnitPrice);


            }
        }

        [TestMethod]
        public void GetAllTest()
        {

            SqlDataReader dr;
            List<Product> expected = new List<Product>();

            _conn.Open();
            using (SqlCommand getCommand = ProductCommandBuilder.MakeReadAll(_conn))
            {
                dr = getCommand.ExecuteReader();
                while (dr.Read())
                {
                    expected.Add(new Product
                    {
                        ProductID = (int) dr["ProductID"],
                        ProductName = (string) dr["ProductName"],
                        UnitPrice = (decimal) dr["UnitPrice"],
                        UnitsInStock = (short) dr["UnitsInStock"]
                    });
                }
            }
            _conn.Close();


            var dao = DaoBuilder.Build<IProductDao>(_connectionString, true);

            List<Product> prods = dao.GetAll().ToList();

            Assert.AreEqual(expected.Count, prods.Count);
            CollectionAssert.AreEqual(expected, prods, new ProductComparer());
        }

        [TestMethod]
        public void UpdateTest()
        {
            string name = "TESTE DE PRODUCTO";
            decimal price = 213.9807m;
            short stock = 50;

            int id;
            using (var ts = new TransactionScope())
            {
                _conn.Open();
                using (SqlCommand insertCommand = ProductCommandBuilder.MakeInsert(_conn))
                {
                    insertCommand.Parameters["@name"].Value = "PRODUCTO DE TESTE";
                    insertCommand.Parameters["@price"].Value = 12345.6789m;
                    insertCommand.Parameters["@stock"].Value = 100;

                    id = (int) insertCommand.ExecuteScalar();
                }
                _conn.Close();


                var dao = DaoBuilder.Build<IProductDao>(_connectionString, true);

                dao.Update(name, price, stock, id);

                _conn.Open();
                using (SqlCommand readCommand = ProductCommandBuilder.MakeRead(_conn))
                {
                    readCommand.Parameters["@id"].Value = id;

                    SqlDataReader dr = readCommand.ExecuteReader();

                    Assert.IsTrue(dr.Read());
                    Assert.AreEqual(id, dr["ProductID"]);
                    Assert.AreEqual(name, dr["ProductName"]);
                    Assert.AreEqual(stock, dr["UnitsInStock"]);
                    Assert.AreEqual(price, dr["UnitPrice"]);

                }
                _conn.Close();


            }
        }

        [TestMethod]
        public void InsertTest()
        {
            string name = "TESTE DE PRODUCTO";
            decimal price = 213.9807m;
            short stock = 50;

            int id;
            using (var ts = new TransactionScope())
            {


                var dao = DaoBuilder.Build<IProductDao>(_connectionString, true);

                id = dao.Insert(name, price, stock);

                _conn.Open();
                using (SqlCommand readCommand = ProductCommandBuilder.MakeRead(_conn))
                {
                    readCommand.Parameters["@id"].Value = id;

                    SqlDataReader dr = readCommand.ExecuteReader();

                    Assert.IsTrue(dr.Read());
                    Assert.AreEqual(id, dr["ProductID"]);
                    Assert.AreEqual(name, dr["ProductName"]);
                    Assert.AreEqual(stock, dr["UnitsInStock"]);
                    Assert.AreEqual(price, dr["UnitPrice"]);

                }
                _conn.Close();


            }
        }

        [TestMethod]
        [ExpectedException(typeof (NotImplementedException))]
        public void NotImplementedTest()
        {

            try
            {
                var dao = DaoBuilder.Build<IProductDao>(_connectionString, true);
                dao.DoNothing("string");

            }
            catch(NotImplementedException ne)
            {
                Assert.AreEqual("The method System.Int32 DoNothing(System.String) is not implemented",ne.Message);
                throw;
            }
            
        }


    }

    public class ProductComparer : IComparer
    {
        public int Compare(object x, object y)
        {
            Product p1 = (Product)x;
            Product p2 = (Product)x;

            if (p1.ProductID == p2.ProductID && p1.ProductName.Equals(p2.ProductName) && p1.UnitPrice == p2.UnitPrice &&
                p1.UnitsInStock == p2.UnitsInStock)
                return 0;
            return -1;
        }
    }
}
