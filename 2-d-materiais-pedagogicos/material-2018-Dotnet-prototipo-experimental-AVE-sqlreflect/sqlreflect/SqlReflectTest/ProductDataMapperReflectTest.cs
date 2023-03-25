using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflectTest.Model;
using SqlReflect;

namespace SqlReflectTest
{
    [TestClass]
    public class ProductDataMapperReflectTest : AbstractProductDataMapperTest
    {
        public ProductDataMapperReflectTest() : base(
            new ReflectDataMapper<int, Product>(typeof(Product), NORTHWIND, true),
            new ReflectDataMapper<int, Category>(typeof(Category), NORTHWIND, true),
            new ReflectDataMapper<int, Supplier>(typeof(Supplier), NORTHWIND, true))
        {
        }

        [TestMethod]
        public void TestProductGetAllReflect()
        {
            base.TestProductGetAll();
        }

        [TestMethod]
        public void TestProductGetByIdReflect()
        {
            base.TestProductGetById();
        }

        [TestMethod]
        public void TestProductInsertAndDeleteReflect()
        {
            base.TestProductInsertAndDelete();
        }
    }
}
