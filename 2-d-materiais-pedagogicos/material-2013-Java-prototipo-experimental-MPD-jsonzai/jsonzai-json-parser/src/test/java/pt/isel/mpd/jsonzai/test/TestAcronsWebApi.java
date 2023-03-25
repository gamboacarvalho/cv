package pt.isel.mpd.jsonzai.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import pt.isel.mpd.jsonzai.JsonParser;
import pt.isel.mpd.jsonzai.test.acron.AcronData;
import pt.isel.mpd.jsonzai.test.acron.AcronDataLfsVar;
import pt.isel.mpd.jsonzai.test.worldweather.WeatherResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.lang.ClassLoader.getSystemResourceAsStream;

/**
 * Created by mcarvalho on 10-04-2015.
 */
public class TestAcronsWebApi extends TestCase{
    public void test_object_AcronDataLfsVar(){
        String src = "{\"lf\": \"adenylyl cyclase\", \"freq\": 1321, \"since\": 1974}";
        JsonParser parser = new JsonParser();
        AcronDataLfsVar lfsVar = parser.toObject(src, AcronDataLfsVar.class);
    }

    public void test_object_AcronData(){
        String src = "{\"sf\": \"AC\", \"lfs\": [{\"lf\": \"adenylyl cyclase\", \"freq\": 1321, \"since\": 1974, \"vars\": [{\"lf\": \"adenylyl cyclase\", \"freq\": 599, \"since\": 1976}, {\"lf\": \"adenylate cyclase\", \"freq\": 587, \"since\": 1975}]}]}";
        JsonParser parser = new JsonParser();
        AcronData data = parser.toObject(src, AcronData.class);
        Assert.assertEquals(1975, data.lfs.get(0).vars.get(1).since);
    }
    public void test_object_AcronDataArray(){
        String src = "[{\"sf\": \"AC\", \"lfs\": [{\"lf\": \"adenylyl cyclase\", \"freq\": 1321, \"since\": 1974, \"vars\": [{\"lf\": \"adenylyl cyclase\", \"freq\": 599, \"since\": 1976}, {\"lf\": \"adenylate cyclase\", \"freq\": 587, \"since\": 1975}]}]}]";
        JsonParser parser = new JsonParser();

        List<AcronData> data = parser.toList(src, AcronData.class);
        Assert.assertEquals(1975, data.get(0).lfs.get(0).vars.get(1).since);
    }

    public void test_object_acronimes_ac() throws IOException {
        try (BufferedReader src = new BufferedReader(
                             new InputStreamReader(
                                     getSystemResourceAsStream("data/acronimes-ac.json")))) {
            JsonParser parser = new JsonParser();
            String json = src.readLine();

            List<AcronData> data = parser.toList(json, AcronData.class);
            long duration = measurePerformance(() -> parser.toList(json, AcronData.class));
            System.out.println(duration + " usecs");

            Assert.assertEquals(1975, data.get(0).lfs.get(0).vars.get(1).since);
            Assert.assertEquals(164, data.get(0).lfs.size());
        }
    }

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
}
