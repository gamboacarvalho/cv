using System;
using System.Linq;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Newtonsoft.Json;
using Jsonzai.Test.Model;
using System.Collections.Generic;
using System.Globalization;
using Jsonzai.Reflect;
using Newtonsoft.Json.Linq;

namespace Jsonzai.Test
{
    [TestClass]
    public class TestJsonfier
    {
        [TestMethod]
        public void TestJsonfierObject()
        {
            var expected = new Student{ nr = 27721, name = "Ze Manel"};
            string json = Jsonfier.ToJson(expected);
            Student actual = JsonConvert.DeserializeObject<Student>(json);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestJsonfierArrayPrimitives()
        {
            int [] expected = { 4, 5, 6, 7};
            string json = Jsonfier.ToJson(expected);
            int[] actual = JsonConvert.DeserializeObject<int[]>(json);
            CollectionAssert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestJsonfierCourse()
        {
            Course expected = new Course
            {
                name = "AVE",
                stds = new Student[4]{
                    new Student{ nr = 27721, name = "Ze Manel"},
                    new Student{ nr = 15642, name = "Maria Papoila"},
                    null,
                    null
                }
            };
            string json = Jsonfier.ToJson(expected);
            Console.WriteLine(json);
            Course actual = JsonConvert.DeserializeObject<Course>(json);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestJsonfierStudentArrayWithMethodsCriteria()
        {
            Student[] expected = {
                new Student{ nr = 27721, name = "Ze Manel"},
                new Student{ nr = 15642, name = "Maria Papoila"},
                null,
                null
            };
            string json = Jsonfier.ToJson(expected, new ReflectorMethods());
            Console.WriteLine(json);
            Student[] actual = JsonConvert.DeserializeObject<Student[]>(json);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestJsonfierRegionInfo()
        {
            RegionInfo expected = new RegionInfo("PT");
            string json = Jsonfier.ToJson(expected);
            Console.WriteLine(json);
            // !!!! RegionInfo does not have a parameterless ctor
            JsonSerializerSettings settings = new JsonSerializerSettings();
            settings.Converters.Add(new RegionInfoConverter());
            RegionInfo actual = JsonConvert.DeserializeObject<RegionInfo>(json, settings);
            Assert.AreEqual(expected, actual);
        }
        /*!!!!!!!!! CICLO INFINITO
        [TestMethod]
        public void TestJsonfierCultureInfo()
        {
            CultureInfo expected = new CultureInfo("pt-PT", false);
            string json = Jsonfier.ToJson(expected);
            Console.WriteLine(json);
            // !!!! CultureInfo does not have a parameterless ctor
            JsonSerializerSettings settings = new JsonSerializerSettings();
            settings.Converters.Add(new CultureInfoConverter());
            CultureInfo actual = JsonConvert.DeserializeObject<CultureInfo>(json, settings);
            Assert.AreEqual(expected, actual);
        }
        */
    }
}
