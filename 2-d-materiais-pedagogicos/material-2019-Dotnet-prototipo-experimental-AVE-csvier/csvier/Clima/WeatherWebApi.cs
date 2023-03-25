using Csvier;
using Request;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Clima
{
    public class WeatherWebApi : IWeatherWebApi, IDisposable
    {
        const string WEATHER_KEY = "10a7e54b547c4c7c870162131192102";
        const string WEATHER_HOST = "http://api.worldweatheronline.com/premium/v1/";
        const string PATH_WEATHER = WEATHER_HOST + "past-weather.ashx?q={0},{1}&date={2}&enddate={3}&tp=24&format=csv&key=" + WEATHER_KEY;
        const string SEARCH = WEATHER_HOST + "search.ashx?query={0}&format=tab&key=" + WEATHER_KEY;

        readonly CsvParser pastWeather;
        readonly CsvParser locations;
        readonly IHttpRequest req;

        public WeatherWebApi() : this(new HttpRequest())
        {
        }

        public WeatherWebApi(IHttpRequest req)
        {
            this.req = req;
            pastWeather = new CsvParser(typeof(WeatherInfo))
                .CtorArg("date", 0)
                .CtorArg("tempC", 2)
                .CtorArg("precipMM", 11)
                .CtorArg("desc", 10);

            locations = new CsvParser(typeof(LocationInfo), '\t')
                .CtorArg("country", 1)
                .CtorArg("region", 2)
                .CtorArg("latitude", 3)
                .CtorArg("longitude", 4);
        }

        public void Dispose()
        {
            req.Dispose();
        }

        public WeatherInfo[] PastWeather(double lat, double log, DateTime from, DateTime to)
        {
            string latStr = lat.ToString("0.000", CultureInfo.InvariantCulture);
            string logStr = log.ToString("0.000", CultureInfo.InvariantCulture);
            string path = String.Format(PATH_WEATHER, latStr, logStr, from.ToShortDateString(), to.ToShortDateString());
            string body = req.GetBody(path);
            object[] items = pastWeather
                    .Load(body)
                    .RemoveWith("#")
                    .Remove(1)
                    .RemoveEvenIndexes()
                    .Parse();
            WeatherInfo[] infos = new WeatherInfo[items.Length];
            for (int i = 0; i < items.Length; i++)
            {
                infos[i] = (WeatherInfo)items[i];
            }
            return infos;
        }

        public LocationInfo[] Search(string query) {
            string path = String.Format(SEARCH, query);
            string body = req.GetBody(path);
            object[] items = locations
                    .Load(body)
                    .RemoveWith("#")
                    .RemoveEmpties()
                    .Parse();
            LocationInfo[] locals = new LocationInfo[items.Length];
            for (int i = 0; i < items.Length; i++)
            {
                locals[i] = (LocationInfo) items[i];
            }
            return locals;
        }
    }
}
