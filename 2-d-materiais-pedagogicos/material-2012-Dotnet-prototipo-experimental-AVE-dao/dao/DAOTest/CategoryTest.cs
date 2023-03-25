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
    public class CategoryTest
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
            string name = "Engineers",
                desc = " Fresh Engineers straight out of college";

            int id;
            using (var ts = new TransactionScope())
            {
                _conn.Open();
                using (SqlCommand insertCommand = CategoryCommandBuilder.MakeInsert(_conn))
                {
                    insertCommand.Parameters["@name"].Value = name;
                    insertCommand.Parameters["@desc"].Value = desc;
                    id = (int)insertCommand.ExecuteScalar();
                }
                _conn.Close();


                ICategoryDao dao = DaoBuilder.Build<ICategoryDao,Category>(_connectionString);

                Category cat = dao.GetById(id);

                Assert.AreEqual(id, cat.CategoryID);
                Assert.AreEqual(name, cat.CategoryName);
                Assert.AreEqual(desc, cat.Description);

            }
        }

        [TestMethod]
        public void GetAllTest()
        {

            SqlDataReader dr;
            List<Category> expected = new List<Category>();

            _conn.Open();
            using (SqlCommand getCommand = CategoryCommandBuilder.MakeReadAll(_conn))
            {
                dr = getCommand.ExecuteReader();
                while (dr.Read())
                {
                    expected.Add(new Category()
                    {
                        CategoryID = (int)dr["CategoryID"],
                        CategoryName = (string)dr["CategoryName"],
                        Description = (string)dr["Description"],
                    });
                }
            }
            _conn.Close();


            var dao = DaoBuilder.Build<ICategoryDao,Category>(_connectionString);

            List<Category> sups = dao.GetAll().ToList();

            Assert.AreEqual(expected.Count, sups.Count);
            CollectionAssert.AreEqual(expected, sups, new CategoryComparer());
        }



        [TestMethod]
        [ExpectedException(typeof(NotImplementedException))]
        public void NotImplementedTest()
        {

            try
            {
                var dao = DaoBuilder.Build<ICategoryDao,Category>(_connectionString);
                dao.GetAllNames();

            }
            catch (NotImplementedException ne)
            {
                Assert.AreEqual("Build<TDao,T> only supports IEnumerable<T>", ne.Message);
                throw;
            }

        }
    }

    public class CategoryComparer : IComparer
    {
        public int Compare(object x, object y)
        {
            Category c1 = (Category)x;
            Category c2 = (Category)x;

           if(c1.CategoryID == c2.CategoryID && c1.CategoryName.Equals(c2.CategoryName) && c1.Description.Equals(c2.Description))
                return 0;
            return -1;
        }
    }
}
