package pt.isel.mpd.jsonzai;

import pt.isel.mpd.jsonzai.monitr.MonitrResp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.ClassLoader.getSystemResourceAsStream;

/**
 * Created by mcarvalho on 09-04-2015.
 */
public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("Cores = " + Runtime.getRuntime().availableProcessors());

        try (BufferedReader src = new BufferedReader(
                new InputStreamReader(
                        getSystemResourceAsStream("data/monitr-market-news-biggest.json")))) {
            System.out.println("Start lexical processing on Json string source...");
            String data = src.readLine();
            measurePerformance(() -> System.out.println(countLexicalTokensSequential(data)));
            measurePerformance(() -> System.out.println(countLexicalTokensParallel(data)));

        }
    }

    public static int countLexicalTokensParallel(String src){
        JsonTokenProviderEager prov = new JsonTokenProviderEager();
        prov.setString(src, false);
        int count = 0;
        try {
            while (true) {
                prov.getNextToken();
                count++;
            }
        }catch(Error e){}
        return count;
    }

    public static int countLexicalTokensSequential(String src){
        JsonTokenProvider prov = new JsonTokenProvider();
        prov.setString(src);
        int count = 0;
        try {
            while (true) {
                prov.getNextToken();
                count++;
            }
        }catch(Error e){}
        return count;
    }

    public static long measurePerformance(Runnable action){
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            action.run();
            long duration = (System.nanoTime() - start) / 1_000;
            System.out.println("..." + duration + " usecs");
            if (duration < fastest) fastest = duration;
        }
        System.out.println("Fastest: " + fastest + " usecs");
        return fastest;
    }

}
