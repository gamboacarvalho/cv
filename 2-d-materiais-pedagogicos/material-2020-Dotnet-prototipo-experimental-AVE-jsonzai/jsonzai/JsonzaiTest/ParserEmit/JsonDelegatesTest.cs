using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Jsonzai.Test.Model;
using Newtonsoft.Json;

namespace Jsonzai.Test.ParserEmit
{

    [TestClass]
    public class JsonDelegatesTest {
        
        [TestMethod]
        public void TestParsingRgbColorUsingJsonPropertyAttribute_WithDelegateConverter() {
            string src = "{\"Red\": \"255\", \"Green\": 100, \"Blue\": 50}";
            JsonParserEmit.AddConverter<RgbColor,Int16>("Red", s => Int16.Parse(s));
            RgbColor color = JsonParserEmit.Parse<RgbColor>(src);
            Assert.AreEqual(255, color.Red);
            Assert.AreEqual(100, color.Green);
            Assert.AreEqual(50, color.Blue);
            JsonParserEmit.ClearConfiguration();
        }

        [TestMethod]
        public void TestParsingRgbColorAnnotatedUsingJsonPropertyAttribute_WithDelegateConverter() {
            string src = "{\"First\": \"255\", \"Second\": 100, \"Third\": 50}";
            JsonParserEmit.AddConverter<RgbColorAnnotated,Int16>("First", JsonDelegatesTest.IntConv);
            RgbColorAnnotated color = JsonParserEmit.Parse<RgbColorAnnotated>(src);
            Assert.AreEqual(255, color.Red);
            Assert.AreEqual(100, color.Green);
            Assert.AreEqual(50, color.Blue);
            JsonParserEmit.ClearConfiguration();
        }

        public static Int16 IntConv(string src) { return Int16.Parse(src); }
        [TestMethod]
        public void TestParsingRgbColorUsingJsonPropertyAttribute_WithMultiDelegateConverter() {
            string src = "{\"Red\": \"255\", \"Green\": 100, \"Blue\": \"50\"}";
            JsonParserEmit.AddConverter<RgbColor, Int16>("Red", s => Int16.Parse(s));
            JsonParserEmit.AddConverter<RgbColor, Int16>("Blue", s => Int16.Parse(s));
            RgbColor color = JsonParserEmit.Parse<RgbColor>(src);
            Assert.AreEqual(255, color.Red);
            Assert.AreEqual(100, color.Green);
            Assert.AreEqual(50, color.Blue);
            JsonParserEmit.ClearConfiguration();
        }

        [TestMethod]
        public void TestParsingRgbColorAnnotatedUsingJsonPropertyAttribute_WithMultiDelegateConverter() {
            string src = "{\"First\": \"255\", \"Second\": 100, \"Third\": \"50\"}";
            JsonParserEmit.AddConverter<RgbColorAnnotated, Int16>("First", s => Int16.Parse(s));
            JsonParserEmit.AddConverter<RgbColorAnnotated, Int16>("Third", s => Int16.Parse(s));
            RgbColorAnnotated color = JsonParserEmit.Parse<RgbColorAnnotated>(src);
            Assert.AreEqual(255, color.Red);
            Assert.AreEqual(100, color.Green);
            Assert.AreEqual(50, color.Blue);
            JsonParserEmit.ClearConfiguration();
        }
    }
}
