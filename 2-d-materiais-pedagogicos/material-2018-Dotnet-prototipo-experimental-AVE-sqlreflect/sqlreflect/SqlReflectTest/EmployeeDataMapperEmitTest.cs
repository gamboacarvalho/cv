using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflect;
using SqlReflectTest.Model;

namespace SqlReflectTest
{
    [TestClass]
    public class EmployeeDataMapperEmitTest : AbstractEmployeeDataMapperTest
    {
        public EmployeeDataMapperEmitTest() : base(EmitDataMapper.Build(typeof(Employee), NORTHWIND, true))
        {
        }

        [TestMethod]
        public void TestEmployeeGetAllEmit() {
            base.TestEmployeeGetAll();
        }
        
    }
}
