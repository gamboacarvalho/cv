using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Net;
using Clima;
using System.Collections.Generic;

namespace Csvier.Test
{
    [TestClass]
    public class CsvParserTest
    {
        string PATH_WEATHER_PORTO_JAN = "http://api.worldweatheronline.com/premium/v1/past-weather.ashx?q=37.017,-7.933&date=2019-01-01&enddate=2019-01-30&tp=24&format=csv&key=10a7e54b547c4c7c870162131192102";
        string SEARCH_OPORTO = "http://api.worldweatheronline.com/premium/v1/search.ashx?query=oporto&format=tab&key=10a7e54b547c4c7c870162131192102";

        [TestMethod]
        public void TestLoadSearchOporto()
        {
            using (WeatherWebApi api = new WeatherWebApi())
            {
                LocationInfo[] locals = api.Search("oporto");
                Assert.AreEqual(6, locals.Length);
                Assert.AreEqual("Cuba", locals[5].Country);
            }
        }

        [TestMethod]
        public void TestLoadPastWeatherOnJanuaryAndCheckMaximumTempC()
        {
            using (WeatherWebApi api = new WeatherWebApi())
            {
                IEnumerable<WeatherInfo> infos = api.PastWeather(37.017, -7.933, DateTime.Parse("2019-01-01"), DateTime.Parse("2019-01-30"));
                int max = int.MinValue;
                foreach (WeatherInfo wi in infos)
                {
                    if (wi.TempC > max) max = wi.TempC;
                }
                Assert.AreEqual(19, max);
                // Console.WriteLine(String.Join("\n", infos));
            }
        }
    }
}
