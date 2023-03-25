package pt.isel.mpd.jsonzai.test.providers;

import junit.framework.Assert;
import junit.framework.TestCase;
import pt.isel.mpd.jsonzai.JsonParser;
import pt.isel.mpd.jsonzai.JsonToken;
import pt.isel.mpd.jsonzai.JsonToken.CurlyBracketOpeningToken;
import pt.isel.mpd.jsonzai.JsonTokenProvider;
import pt.isel.mpd.jsonzai.JsonTokenProviderEager;
import pt.isel.mpd.jsonzai.test.worldweather.WeatherResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.ClassLoader.getSystemResourceAsStream;

/**
 * Created by mcarvalho on 22-05-2015.
 */
public class JsonTokenProviderTest extends TestCase {


    public static long measurePerformance(Runnable action) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            action.run();
            long duration = (System.nanoTime() - start) / 1_000;
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }

    public void test_short_sequencial_tokens() throws Exception {
        try (BufferedReader data =
                     new BufferedReader(
                             new InputStreamReader(
                                     getSystemResourceAsStream("data/weather-request.json")))) {

            JsonTokenProvider prov = new JsonTokenProvider();
            prov.setString(data.readLine());

            Assert.assertEquals(CurlyBracketOpeningToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("query", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("Lisbon, Portugal", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.CommaToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("type", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("City", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.CurlyBracketClosingToken.INSTANCE, prov.getNextToken());
        }
    }

    public void test_short_eager_tokens() throws Exception {
        try (BufferedReader data =
                     new BufferedReader(
                             new InputStreamReader(
                                     getSystemResourceAsStream("data/weather-request.json")))) {

            JsonTokenProviderEager prov = new JsonTokenProviderEager();
            prov.setString(data.readLine(), false);

            Assert.assertEquals(CurlyBracketOpeningToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("query", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("Lisbon, Portugal", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.CommaToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("type", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("City", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.CurlyBracketClosingToken.INSTANCE, prov.getNextToken());
        }
    }

    public void test_medium_eager_parallel_tokens() throws Exception {
        try (BufferedReader data =
                     new BufferedReader(
                             new InputStreamReader(
                                     getSystemResourceAsStream("data/weather-objects.json")))) {

            JsonTokenProviderEager prov = new JsonTokenProviderEager();
            prov.setString(data.readLine(), true);

            Assert.assertEquals(JsonToken.SquareBracketOpeningToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals(JsonToken.CurlyBracketOpeningToken.INSTANCE, prov.getNextToken());

            Assert.assertEquals("maxtempC", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("15", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.CommaToken.INSTANCE, prov.getNextToken());

            Assert.assertEquals("maxtempF", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("59", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.CommaToken.INSTANCE, prov.getNextToken());

            Assert.assertEquals("mintempC", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("11", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.CommaToken.INSTANCE, prov.getNextToken());

            Assert.assertEquals("mintempF", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("52", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.CommaToken.INSTANCE, prov.getNextToken());

            Assert.assertEquals("uvIndex", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("0", prov.getNextToken().getValue());

            Assert.assertEquals(JsonToken.CurlyBracketClosingToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals(JsonToken.CommaToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals(JsonToken.CurlyBracketOpeningToken.INSTANCE, prov.getNextToken());

            Assert.assertEquals("astronomy", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("moon", prov.getNextToken().getValue());

            Assert.assertEquals(JsonToken.CurlyBracketClosingToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals(JsonToken.CommaToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals(JsonToken.CurlyBracketOpeningToken.INSTANCE, prov.getNextToken());

            Assert.assertEquals("cloudcover", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("14", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.CommaToken.INSTANCE, prov.getNextToken());

            Assert.assertEquals("DewPointC", prov.getNextToken().getValue());
            Assert.assertEquals(JsonToken.ColonToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals("7", prov.getNextToken().getValue());

            Assert.assertEquals(JsonToken.CurlyBracketClosingToken.INSTANCE, prov.getNextToken());
            Assert.assertEquals(JsonToken.SquareBracketClosingToken.INSTANCE, prov.getNextToken());

            try{
                prov.getNextToken();
            } catch(Error e){
                Assert.assertTrue(true);
                return;
            }
            Assert.assertTrue(false);
        }
    }

    public void test_weather_lisbon_with_sequencial_provider () throws Exception{
        try (BufferedReader data =
                     new BufferedReader(
                             new InputStreamReader(
                                     getSystemResourceAsStream("data/monitr-market-news-biggest.json")))) {

            JsonTokenProvider prov = new JsonTokenProvider();
            prov.setString(data.readLine());

            int count = 0;
            try {
                while (true) {
                    prov.getNextToken();
                    count++;
                }
            }catch(Error e){

            }
            Assert.assertEquals(1057705, count);
        }
    }

    public void test_weather_lisbon_with_eager_paralel () throws Exception{
        try (BufferedReader data =
                     new BufferedReader(
                             new InputStreamReader(
                                     getSystemResourceAsStream("data/monitr-market-news-biggest.json")))) {

            JsonTokenProviderEager prov = new JsonTokenProviderEager();
            prov.setString(data.readLine(), true);

            int count = 0;
            try {
                while (true) {
                    prov.getNextToken();
                    count++;
                }
            }catch(Error e){

            }
            Assert.assertEquals(1057705, count);
        }
    }
}