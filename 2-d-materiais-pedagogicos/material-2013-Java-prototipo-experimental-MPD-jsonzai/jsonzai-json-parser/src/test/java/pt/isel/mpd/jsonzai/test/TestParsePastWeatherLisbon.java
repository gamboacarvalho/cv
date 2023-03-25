package pt.isel.mpd.jsonzai.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import pt.isel.mpd.jsonzai.JsonParser;
import pt.isel.mpd.jsonzai.test.worldweather.WeatherResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.ClassLoader.getSystemResourceAsStream;

/**
 * Created by mcarvalho on 08-04-2015.
 */
public class TestParsePastWeatherLisbon extends TestCase{


    public static long measurePerformance(Runnable action){
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            action.run();
            long duration = (System.nanoTime() - start) / 1_000;
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }

    public void test_parse_objects() throws IOException {
        try (BufferedReader data =
                     new BufferedReader(
                             new InputStreamReader(
                                     getSystemResourceAsStream("data/past-weather-lisbon.json")))) {
            String src = data.readLine();
            JsonParser parser = new JsonParser();

            WeatherResponse wd = parser.toObject(src, WeatherResponse.class);
            long duration = measurePerformance(() -> parser.toObject(src, WeatherResponse.class));
            System.out.println(duration + " usecs");

            Assert.assertEquals(29, wd.data.weather.size());
            Assert.assertEquals(14, wd.data.weather.get(2).maxtempC);
            Assert.assertEquals(7, wd.data.weather.get(2).mintempC);
            Assert.assertEquals("Partly Cloudy", wd.data.weather.get(5).hourly.get(0).weatherDesc.get(0).value);
        }
    }
}