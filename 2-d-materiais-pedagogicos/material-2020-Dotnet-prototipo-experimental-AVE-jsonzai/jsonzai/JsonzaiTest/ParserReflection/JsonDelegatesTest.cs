using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Jsonzai.Test.Model;
using Newtonsoft.Json;

namespace Jsonzai.Test.ParserReflection
{

    [TestClass]
    public class JsonDelegatesTest {
        
        [TestMethod]
        public void TestParsingRgbColorUsingJsonPropertyAttribute_WithDelegateConverter() {
            string src = "{\"Red\": \"255\", \"Green\": 100, \"Blue\": 50}";
            JsonParser.AddConverter<RgbColor,Int16>("Red", s => Int16.Parse(s));
            RgbColor color = JsonParser.Parse<RgbColor>(src);
            Assert.AreEqual(255, color.Red);
            Assert.AreEqual(100, color.Green);
            Assert.AreEqual(50, color.Blue);
            JsonParser.ClearConfiguration();
        }

        [TestMethod]
        public void TestParsingRgbColorAnnotatedUsingJsonPropertyAttribute_WithDelegateConverter() {
            string src = "{\"First\": \"255\", \"Second\": 100, \"Third\": 50}";
            JsonParser.AddConverter<RgbColorAnnotated,Int16>("First", s => Int16.Parse(s));
            RgbColorAnnotated color = JsonParser.Parse<RgbColorAnnotated>(src);
            Assert.AreEqual(255, color.Red);
            Assert.AreEqual(100, color.Green);
            Assert.AreEqual(50, color.Blue);
            JsonParser.ClearConfiguration();
        }

        [TestMethod]
        public void TestParsingRgbColorUsingJsonPropertyAttribute_WithMultiDelegateConverter() {
            string src = "{\"Red\": \"255\", \"Green\": 100, \"Blue\": \"50\"}";
            JsonParser.AddConverter<RgbColor, Int16>("Red", s => Int16.Parse(s));
            JsonParser.AddConverter<RgbColor, Int16>("Blue", s => Int16.Parse(s));
            RgbColor color = JsonParser.Parse<RgbColor>(src);
            Assert.AreEqual(255, color.Red);
            Assert.AreEqual(100, color.Green);
            Assert.AreEqual(50, color.Blue);
            JsonParser.ClearConfiguration();
        }

        [TestMethod]
        public void TestParsingRgbColorAnnotatedUsingJsonPropertyAttribute_WithMultiDelegateConverter() {
            string src = "{\"First\": \"255\", \"Second\": 100, \"Third\": \"50\"}";
            JsonParser.AddConverter<RgbColorAnnotated, Int16>("First", s => Int16.Parse(s));
            JsonParser.AddConverter<RgbColorAnnotated, Int16>("Third", s => Int16.Parse(s));
            RgbColorAnnotated color = JsonParser.Parse<RgbColorAnnotated>(src);
            Assert.AreEqual(255, color.Red);
            Assert.AreEqual(100, color.Green);
            Assert.AreEqual(50, color.Blue);
            JsonParser.ClearConfiguration();
        }
    }
}
