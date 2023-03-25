using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflect;
using SqlReflectTest.Model;

namespace SqlReflectTest
{
    [TestClass]
    public class EmployeeDataMapperReflectTest : AbstractEmployeeDataMapperTest
    {
        public EmployeeDataMapperReflectTest() : base(new ReflectDataMapper<int, Employee>(typeof(Employee), NORTHWIND, true))
        {
        }

        [TestMethod]
        public void TestEmployeeGetAllReflect() {
            base.TestEmployeeGetAll();
        }
        
    }
}
