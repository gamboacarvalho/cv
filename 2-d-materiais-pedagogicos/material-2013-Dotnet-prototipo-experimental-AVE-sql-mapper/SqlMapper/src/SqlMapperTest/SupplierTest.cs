using Isel.Ave14.SqlMapper;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;

namespace SqlMapperTest
{
    [TestClass]
    public class SupplierTest
    {

        static readonly SqlConnectionStringBuilder builder;
        ISqlExecutorFactory fac;

        static SupplierTest()
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
            IDataMapper<Supplier> supMapper = b.Build<Supplier>();
            IEnumerable<Supplier> prods = supMapper.GetAll();
            Assert.AreEqual(30, prods.Count());
        }

        [TestMethod]
        public void test_fk_relation_with_products()
        {
            Builder b = new Builder(fac);
            IDataMapper<Supplier> supMapper = b.Build<Supplier>();
            Supplier sup = supMapper.GetAll().Where("SupplierId = 7").First();
            Assert.AreEqual("Pavlova, Ltd.", sup.CompanyName);
            Assert.AreEqual(5, sup.Products.Count());
        }
    }
}
