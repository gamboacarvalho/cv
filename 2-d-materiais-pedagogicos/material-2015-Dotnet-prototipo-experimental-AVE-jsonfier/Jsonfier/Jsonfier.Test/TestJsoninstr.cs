using System;
using System.Text;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Newtonsoft.Json;
using Jsonzai.Instr;
using Jsonzai.Test.Model;
using System.Globalization;

namespace Jsonzai.Test
{
    [TestClass]
    public class TestJsoninstr
    {
        [TestMethod]
        public void TestJsoninstrObject()
        {
            var expected = new Student { nr = 27721, name = "Ze Manel" };
            string json = Jsoninstr.ToJson(expected);
            Student actual = JsonConvert.DeserializeObject<Student>(json);
            Assert.AreEqual(expected, actual);
        }

        [TestMethod]
        public void TestJsoninstrCourse()
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
            string json = Jsoninstr.ToJson(expected);
            Console.WriteLine(json);
            Course actual = JsonConvert.DeserializeObject<Course>(json);
            Assert.AreEqual(expected, actual);
        }
        [TestMethod]
        public void TestJsoninstrRegionInfo()
        {
            RegionInfo expected = new RegionInfo("PT");
            string json = Jsoninstr.ToJson(expected);
            Console.WriteLine(json);
            // !!!! RegionInfo does not have a parameterless ctor
            // !!!! RegionInfo does not have a parameterless ctor
            JsonSerializerSettings settings = new JsonSerializerSettings();
            settings.Converters.Add(new RegionInfoConverter());
            RegionInfo actual = JsonConvert.DeserializeObject<RegionInfo>(json, settings);
            Assert.AreEqual(expected, actual);
        }
        
    }
}
