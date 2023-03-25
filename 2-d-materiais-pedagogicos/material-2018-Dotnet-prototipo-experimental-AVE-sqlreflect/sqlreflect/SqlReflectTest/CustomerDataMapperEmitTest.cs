using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SqlReflect;
using SqlReflectTest.Model;

namespace SqlReflectTest
{
    [TestClass]
    public class CustomerDataMapperEmitTest : AbstractCustomerDataMapperTest
    {
        public CustomerDataMapperEmitTest() : base(EmitDataMapper.Build(typeof(Customer), NORTHWIND, true))
        {
        }

        [TestMethod]
        public void TestCustomerGetAllEmit() {
            base.TestCustomerGetAll();
        }
        
    }
}
