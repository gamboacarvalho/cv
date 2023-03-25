using Request;
using System;

namespace Clima
{
    public interface IWeatherWebApi : IDisposable
    {
        LocationInfo[] Search(string query);
        WeatherInfo[] PastWeather(double lat, double log, DateTime from, DateTime to);
    }
}