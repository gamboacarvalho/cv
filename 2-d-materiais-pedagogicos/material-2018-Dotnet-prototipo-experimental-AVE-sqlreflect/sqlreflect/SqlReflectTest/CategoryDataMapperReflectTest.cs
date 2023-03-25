using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflect;
using SqlReflectTest.Model;

namespace SqlReflectTest
{
    [TestClass]
    public class CategoryDataMapperReflectTest : AbstractCategoryDataMapperTest
    {
        public CategoryDataMapperReflectTest() : base(new ReflectDataMapper<int, Category>(typeof(Category), NORTHWIND, true))
        {
        }

        [TestMethod]
        public void TestCategoryGetAllReflect() {
            base.TestCategoryGetAll();
        }

        [TestMethod]
        public void TestCategoryGetByIdReflect() {
            base.TestCategoryGetById();
        }
        [TestMethod]
        public void TestCategoryInsertAndDeleteReflect()
        {
            base.TestCategoryInsertAndDelete();
        }

        [TestMethod]
        public void TestCategoryUpdateReflect()
        {
            base.TestCategoryUpdate();
        }
    }
}
