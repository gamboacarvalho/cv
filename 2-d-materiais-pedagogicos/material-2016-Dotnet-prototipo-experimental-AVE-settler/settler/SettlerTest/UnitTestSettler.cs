using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections;
using System.Collections.Generic;

namespace Settler.Test
{
    [TestClass]
    public class UnitTestSettler
    {
        [TestMethod]
        public void TestNew()
        {
            Fixture<Student> fix = AutoFixture.For<Student>();
            Student s = fix.New();
            Assert.IsNotNull(s);
            Assert.IsNotNull(s.Name);
            Assert.AreNotEqual(0, s.Nr);
            Console.WriteLine(s);
        }

        [TestMethod]
        public void TestMember()
        {
            String[] expectedNames = { "Jose Calhau", "Maria Papoila", "Augusto Seabra" };
            Object[] expectedNrs = { 8713, 2312, 23123, 131, 54534 };
            Fixture<Student> fix = AutoFixture
                .For<Student>()
                .Member("name", expectedNames) // Field or property with the name Name
                .Member("nr", expectedNrs);   // Field or property with the name Nr
            Student s = fix.New(); // The properties Name and Nr have one of above values
            CollectionAssert.Contains(expectedNames, s.Name);
            CollectionAssert.Contains(expectedNrs, s.Nr);
            Console.WriteLine(s);
        }

        [TestMethod]
        public void TestIgnoreMember()
        {
            Fixture<Student> fix = AutoFixture
                .For<Student>()
                .Ignore("Name");
            Student s = fix.New(); // Não afecta a propriedade Name
        }

        class NonFixtureAttribute : Attribute {
        }

        [TestMethod]
        public void TestIgnoreMemberWithCustomAttribute()
        {
            Fixture<Student> fix = AutoFixture
                .For<Student>()
                .Ignore<NonFixtureAttribute>();
            Student s = fix.New(); // Não propriedades anotadas com NonFixture
        }

        [TestMethod]
        public void TestMemberSupplierDate()
        {
            Random rand = new Random();
            DateTime dt = new DateTime(1970, 1, 1);
            Fixture<Student> fix = AutoFixture
                .For<Student>()
                .Member("BirthDate", () => dt.AddMonths(rand.Next(600)));
        }

        [TestMethod]
        public void TestMemberSupplier()
        {
            Fixture<Student> fix = AutoFixture
                .For<Student>()
                .Member("name", () => "Leonor Garrido")
                .Member("nr", () => 87236);
            Student s = fix.New();
            Assert.AreEqual("Leonor Garrido", s.Name);
            Assert.AreEqual(87236, s.Nr);
        }

        [TestMethod]
        public void TestFill()
        {
            Fixture<Student> fix = AutoFixture.For<Student>();
            Student[] res = fix.Fill(7);
            foreach (Student s in res)
            {
                Assert.IsNotNull(s);
                Assert.IsNotNull(s.Name);
                Assert.AreNotEqual(0, s.Nr);
            }
        }

        [TestMethod]
        public void TestComplexDomain()
        {
            Fixture<Classroom> fix = AutoFixture.For<Classroom>();
            Classroom cr = fix.New();
            Assert.AreNotEqual(0, cr.students.Length);
            foreach (Student s in cr.students)
            {
                Assert.IsNotNull(s);
                Assert.IsNotNull(s.Name);
                Assert.AreNotEqual(0, s.Nr);
            }
            Console.WriteLine(cr);
        }
    }
}
