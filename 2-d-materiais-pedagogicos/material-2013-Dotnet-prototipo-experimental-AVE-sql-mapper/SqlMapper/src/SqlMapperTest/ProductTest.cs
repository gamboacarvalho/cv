using Isel.Ave14.SqlMapper;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;

namespace SqlMapperTest
{
    [TestClass]
    public class ProductTest
    {

        static readonly SqlConnectionStringBuilder builder;
        ISqlExecutorFactory fac;

        static ProductTest()
        {
            builder = new SqlConnectionStringBuilder();
            builder.DataSource = @"DRAGAO\SQLEXPRESS";
            builder.IntegratedSecurity = true;
            builder.InitialCatalog = "Northwind";
        }

        [TestInitialize]
        public void Setup()
        {
            fac =
               new SqlExecutorSingleCallFactory(
                   builder.ConnectionString);
        }

        [TestCleanup]
        public void TearDown()
        {
            fac.Dispose();
        }

        [TestMethod]
        public void test_getAll()
        {
            Builder b = new Builder(fac);
            IDataMapper<Product> prodMapper = b.Build<Product>();
            IEnumerable<Product> prods = prodMapper.GetAll();
            Assert.AreEqual(77, prods.Count());
        }

        [TestMethod]
        public void test_get_and_where_by_categoryId()
        {
            Builder b = new Builder(fac);
            IDataMapper<Product> prodMapper = b.Build<Product>();
            IEnumerable<Product> prods = prodMapper.GetAll().Where("CategoryID = 7");
            Assert.AreEqual(5, prods.Count());
        }

        [TestMethod]
        public void test_get_and_double_where()
        {
            Builder b = new Builder(fac);
            IDataMapper<Product> prodMapper = b.Build<Product>();
            IEnumerable<Product> prods = prodMapper
                .GetAll()
                .Where("CategoryID = 7")
                .Where("UnitsinStock > 30");
            Assert.AreEqual(1, prods.Count());
            Assert.AreEqual("Tofu", prods.First().ProductName);

        }

    }
}
