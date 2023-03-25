package pt.isel.mpd.jsonzai.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import pt.isel.mpd.jsonzai.JsonParser;
import pt.isel.mpd.jsonzai.test.worldweather.WeatherDetails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.lang.ClassLoader.getSystemResourceAsStream;

/**
 * Created on 08-04-2015.
 * @author Miguel Gamboa at CCISEL
 */
public class TestParseArray extends TestCase{

    public void test_parse_array() throws IOException {
        try (BufferedReader data =
                     new BufferedReader(
                             new InputStreamReader(getSystemResourceAsStream("data/weather-objects.json")))) {
            String src = data.readLine();
            JsonParser parser = new JsonParser();

            List<WeatherDetails> res = parser.toList(src, WeatherDetails.class);
            WeatherDetails wd = res.get(0);
            Assert.assertEquals(15, wd.maxtempC);
            Assert.assertEquals(11, wd.mintempC);
        }

    }
}
