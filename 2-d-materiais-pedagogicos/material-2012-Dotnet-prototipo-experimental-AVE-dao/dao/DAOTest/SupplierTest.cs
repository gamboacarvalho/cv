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
    public class SupplierTest
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
            string companyName = "ISEL",
                contactName = " Matilde Teste",
                title = "Presidente",
                address = "Rua Emidio Qualquer Coisa",
                city = "Lisboa",
                country = "Portugal";

            int id;
            using (var ts = new TransactionScope())
            {
                _conn.Open();
                using (SqlCommand insertCommand = SupplierCommandBuilder.MakeInsert(_conn))
                {
                    insertCommand.Parameters["@companyName"].Value = companyName;
                    insertCommand.Parameters["@contactName"].Value = contactName;
                    insertCommand.Parameters["@title"].Value = title;
                    insertCommand.Parameters["@address"].Value = address;
                    insertCommand.Parameters["@city"].Value = city;
                    insertCommand.Parameters["@country"].Value = country;

                    id = (int)insertCommand.ExecuteScalar();
                }
                _conn.Close();


                ISupplierDao dao = DaoBuilder.Build<ISupplierDao>(_connectionString);

                Supplier sup = dao.GetById(id);

                Assert.AreEqual(id, sup.SupplierID);
                Assert.AreEqual(companyName, sup.CompanyName);
                Assert.AreEqual(contactName, sup.ContactName);
                Assert.AreEqual(title, sup.ContactTitle);
                Assert.AreEqual(address, sup.Address);
                Assert.AreEqual(city, sup.City);
                Assert.AreEqual(country, sup.Country);

            }
        }

        [TestMethod]
        public void GetAllTest()
        {

            SqlDataReader dr;
            List<Supplier> expected = new List<Supplier>();

            _conn.Open();
            using (SqlCommand getCommand = SupplierCommandBuilder.MakeReadAll(_conn))
            {
                dr = getCommand.ExecuteReader();
                while (dr.Read())
                {
                    expected.Add(new Supplier()
                    {
                        SupplierID = (int)dr["SupplierID"],
                        CompanyName = (string)dr["CompanyName"],
                        Address = (string)dr["Address"],
                        Country = (string)dr["Country"]
                    });
                }
            }
            _conn.Close();


            var dao = DaoBuilder.Build<ISupplierDao>(_connectionString);

            List<Supplier> sups = dao.GetAll().ToList();

            Assert.AreEqual(expected.Count, sups.Count);
            CollectionAssert.AreEqual(expected, sups, new SupplierComparer());
        }

        [TestMethod]
        public void DeleteTest()
        {
            string companyName = "ISEL",
               contactName = " Matilde Teste",
               title = "Presidente",
               address = "Rua Emidio Qualquer Coisa",
               city = "Lisboa",
               country = "Portugal";

            int id;
            using (var ts = new TransactionScope())
            {
                _conn.Open();
                using (SqlCommand insertCommand = SupplierCommandBuilder.MakeInsert(_conn))
                {
                    insertCommand.Parameters["@companyName"].Value = companyName;
                    insertCommand.Parameters["@contactName"].Value = contactName;
                    insertCommand.Parameters["@title"].Value = title;
                    insertCommand.Parameters["@address"].Value = address;
                    insertCommand.Parameters["@city"].Value = city;
                    insertCommand.Parameters["@country"].Value = country;

                    id = (int)insertCommand.ExecuteScalar();
                }
                _conn.Close();


                var dao = DaoBuilder.Build<ISupplierDao>(_connectionString);

                dao.Delete(id);

                _conn.Open();
                using (SqlCommand readCommand = SupplierCommandBuilder.MakeRead(_conn))
                {
                    readCommand.Parameters["@id"].Value = id;

                    SqlDataReader dr = readCommand.ExecuteReader();

                    Assert.IsFalse(dr.Read());


                }
                _conn.Close();

            }
        }

        [TestMethod]
        public void GetAllCountriesTest()
        {
            List<string> expected = new List<string>();

            _conn.Open();
            using (SqlCommand getCommand = SupplierCommandBuilder.MakeReadAllCountries(_conn))
            {
                SqlDataReader dr = getCommand.ExecuteReader();
                while (dr.Read())
                {
                    expected.Add((string)dr["Country"]);
                }
            }
            _conn.Close();


            var dao = DaoBuilder.Build<ISupplierDao>(_connectionString);

            List<string> sups = dao.GetAllCountries().ToList();

            Assert.AreEqual(expected.Count, sups.Count);
            CollectionAssert.AreEqual(expected, sups);
        }

        [TestMethod]
        [ExpectedException(typeof(NotImplementedException))]
        public void NotImplementedTest()
        {

            try
            {
                var dao = DaoBuilder.Build<ISupplierDao>(_connectionString, true);
                dao.DoNothing("string",2);

            }
            catch (NotImplementedException ne)
            {
                Assert.AreEqual("The method System.Void DoNothing(System.String, System.Int32) is not implemented", ne.Message);
                throw;
            }

        }
    }

    public class SupplierComparer : IComparer
    {
        public int Compare(object x, object y)
        {
            Supplier s1 = (Supplier)x;
            Supplier s2 = (Supplier)x;

            if(s1.SupplierID == s2.SupplierID && s1.CompanyName.Equals(s2.CompanyName) && s1.Address.Equals(s2.Address) && s1.Country.Equals(s2.Country))
                return 0;
            return -1;
        }
    }
}
