package pt.isel.mpd.jsonzai.test.providers;


import junit.framework.Assert;
import junit.framework.TestCase;
import pt.isel.mpd.jsonzai.JsonTokenPosition;
import pt.isel.mpd.jsonzai.JsonTokenSpliterator;
import pt.isel.mpd.jsonzai.JsonTokenSpliterator.ToListCollector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class JsonTokenSpliteratorTest extends TestCase{

    public void test_spliterator() throws Exception {
        try (BufferedReader data =
                     new BufferedReader(
                             new InputStreamReader(
                                     getSystemResourceAsStream("data/weather-objects.json")))) {

            List<JsonTokenPosition> res = stream
                    (new JsonTokenSpliterator(data.readLine()), false)
                        .collect(toList());

            Assert.assertEquals(53, res.size());
        }
    }

    public void test_spliterator_parallel() throws Exception {
        try (BufferedReader data =
                     new BufferedReader(
                             new InputStreamReader(
                                     getSystemResourceAsStream("data/weather-objects.json")))) {

            String src = data.readLine();

            List<JsonTokenPosition> res = stream
                    (new JsonTokenSpliterator(src), true)
                    .collect(new ToListCollector<JsonTokenPosition>());
/*
            System.out.println(res.get(0).tokenProvider.apply(0, src));
            System.out.println(res.get(1));
            System.out.println(res.get(0));
*/
            res.stream().forEach(p -> System.out.println( p.idx));

            Assert.assertEquals(54, res.size());
        }
    }
}
