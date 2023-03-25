using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Mapper;
using MapperTest.Model;
using System.Collections.Generic;
using MapperTest;

namespace MapperTest
{
    [TestClass]
    public class TestMapperObjectModel
    {
        [TestMethod]
        public void TestStudent()
        {
            Mapper<Student, Person> m = AutoMapper 
                .Build<Student, Person>()
                .ForMember("Id", src => src.Nr.ToString())
                .CreateMapper();
            var s = new Student { nr = 27721, name = "Ze Manel" };
            Person p = m.Map(s);
            Assert.AreEqual(s.Name, p.Name);
            Assert.AreEqual(s.Nr.ToString(), p.Id);
        }

        [TestMethod]
        public void TestStudentsCollection()
        {
            Student[] stds = {
                    new Student{ nr = 27721, name = "Ze Manel"},
                    new Student{ nr = 15642, name = "Maria Papoila"}};
            Person[] expected = {
                    new Person{ Id = "27721", Name = "Ze Manel"},
                    new Person{ Id = "15642", Name = "Maria Papoila"}};
            Mapper<Student, Person> m = AutoMapper
                .Build<Student, Person>()
                .ForMember("Id", src => src.Nr.ToString())
                .CreateMapper();

            List<Person> ps = m.Map<List<Person>>(stds);
            
            CollectionAssert.AreEqual(expected, ps.ToArray());

            Assert.AreEqual(stds[0].Name, ps[0].Name);
            Assert.AreEqual(stds[0].Nr.ToString(), ps[0].Id);
            Assert.AreEqual(stds[1].Name, ps[1].Name);
            Assert.AreEqual(stds[1].Nr.ToString(), ps[1].Id);
        }


        [TestMethod]
        public void TestIgnorePropertyName()
        {
            Mapper<Student, Person> m = AutoMapper
                .Build<Student, Person>()
                .IgnoreMember("Name")
                .CreateMapper();
        }

        [TestMethod]
        public void TestIgnorePropertyAttr()
        {
            Mapper<Student, Person> m = AutoMapper
                .Build<Student, Person>()
                .IgnoreMember<AvoidMapping>()
                .CreateMapper();
        }

        public void Test()
        {
            int[] src = {45, 23, 4, 56, 2, 346};
            ArrayIndexer<int> f1 = new ArrayIndexer<int>(src);
            Console.WriteLine(f1.ElementAt(3)); // => 56
            Indexer f2 = (Indexer) f1;
            Console.WriteLine(f2.ElementAt(3)); // => 56
        }
    }
    class AvoidMapping : Attribute{ }

    
interface Indexer<T> : Indexer
{
    new T ElementAt(int index);
}

interface Indexer
{
    object ElementAt(int index);
}

class ArrayIndexer<T> : Indexer<T>
{
    private T[] src;

    public ArrayIndexer(T[] src)
    {
        this.src = src;
    }

    public T ElementAt(int iteration)
    {
        throw new NotImplementedException();
    }

    object Indexer.ElementAt(int iteration)
    {
        throw new NotImplementedException();
    }
}

}
