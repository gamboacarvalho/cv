package pt.isel.mpd.jsonzai.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import pt.isel.mpd.jsonzai.JsonParser;
import pt.isel.mpd.jsonzai.test.monitr.MonitrResp;
import pt.isel.mpd.jsonzai.test.monitr.MonitrRespData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.ClassLoader.getSystemResourceAsStream;

/**
 * Created by mcarvalho on 13-04-2015.
 */
public class TestMonitrApi extends TestCase{

    public void test_monitr_data_object(){
        String src = "{\"market\":\"dji\",\"title\":\"Winding Down GE\",\"symbol\":\"GE\",\"link\":\"seekingalpha.com\",\"sentiment\":14,\"time\":1428831702000,\"rank\":\"1,095\",\"secondary\":\"325\",\"domain\":\"seekingalpha.com\"}";
        JsonParser parser = new JsonParser();
        MonitrRespData res = (MonitrRespData) parser.toObject(src, MonitrRespData.class);
    }
    public void test_object_MonitrResp() throws IOException {
        System.out.println("Cores = " + Runtime.getRuntime().availableProcessors());

        try (BufferedReader src = new BufferedReader(
                new InputStreamReader(
                        getSystemResourceAsStream("data/monitr-market-news.json")))) {
            JsonParser parser = new JsonParser();
            String json = src.readLine();
            MonitrResp res = parser.toObject(json, MonitrResp.class);
            long duration = measurePerformance(() -> parser.toObject(json, MonitrResp.class));
            System.out.println(duration + " usecs");

            Assert.assertEquals(1000, res.data.size());
            Assert.assertEquals("www.marketwatch.com", res.data.get(999).domain);

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
