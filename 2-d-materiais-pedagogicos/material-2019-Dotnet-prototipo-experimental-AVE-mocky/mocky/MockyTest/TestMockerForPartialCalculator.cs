using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Request;

namespace Mocky.Test
{
    [TestClass]
    public class TestMockerForPartialCalculator
    {
        readonly ICalculator calc;

        public TestMockerForPartialCalculator()
        {
            Mocker mock = new Mocker(typeof(ICalculator));
            mock.When("Add").With(5, 7).Return(12);
            mock.When("Add").With(3, 4).Return(7);
            mock.When("Mul").With(3, 3).Return(9);
            calc = (ICalculator)mock.Create();
        }
        
        [TestMethod]
        public void TestCalculatorSuccessfully()
        {
            Assert.AreEqual(calc.Add(5, 7), 12);
            Assert.AreEqual(calc.Add(3, 4), 7);
            Assert.AreEqual(calc.Add(4, 1), 0); // Returns 0 rather than 5 because that behavior was not defined for Add
            Assert.AreEqual(calc.Mul(3, 3), 9);
        }

        [TestMethod]
        [ExpectedException(typeof(NotImplementedException))]
        public void TestCalculatorFailing()
        {
            Assert.AreEqual(calc.Sub(2, 1), 1); // NotImplementedException
        }

        [TestMethod]
        public void TestCalculatorThen()
        {
            Mocker mockCalc = new Mocker(typeof(ICalculator));
            mockCalc.When("Add").Then<int, int, int>((a,b) => a + b);
            Mocker mockReq = new Mocker(typeof(IHttpRequest));
            mockReq.When("GetBody").Then<string, string>(url => "");
//             mockReq.When("Dispose").Then(() => {/* do nothing */});

            mockCalc.When("Add").Then<int, int, double>((a, b) => a + b);

            // mockReq.When("Dispose").Then<string>((arg) => {/* do nothing */});

        }
    }
}
