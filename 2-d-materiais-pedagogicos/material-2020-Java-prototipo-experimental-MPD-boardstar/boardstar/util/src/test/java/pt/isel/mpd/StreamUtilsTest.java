package pt.isel.mpd;

import org.junit.Test;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Stream.generate;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static pt.isel.mpd.util.streams.StreamUtils.*;

public class StreamUtilsTest {

    @Test
    public void testCache() {
        Random r = new Random();
        Stream<Integer> nrsStream = generate(() -> r.nextInt(100));
        Supplier<Stream<Integer>> nrs = cache(nrsStream);
        Object[] expected = nrs.get().limit(10).toArray();
        Object[] actual = nrs.get().limit(10).toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testInterleave_smallerFist() {
        Stream<String> stream1 = Stream.of("1", "2", "3");
        Stream<String> stream2 = Stream.of("a", "b", "c", "d", "e", "f");
        Stream<String> res = interleave(stream1, stream2);
        Object[] expected = new String[]{"1", "a", "2", "b", "3", "c", "d", "e", "f"};
        Object[] actual = res.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testInterleave_smallerSecond() {
        Stream<String> stream1 = Stream.of("a", "b", "c", "d", "e", "f");
        Stream<String> stream2 = Stream.of("1", "2", "3");
        Stream<String> res = interleave(stream1, stream2);
        Object[] expected = new String[]{"a", "1", "b", "2", "c", "3", "d", "e", "f"};
        Object[] actual = res.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testInterleave_noElemsFist() {
        Stream<String> stream1 = Stream.of();
        Stream<String> stream2 = Stream.of("a", "b", "c", "d", "e", "f");
        Stream<String> res = interleave(stream1, stream2);
        Object[] expected = new String[]{"a", "b", "c", "d", "e", "f"};
        Object[] actual = res.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testInterleave_noElemsSecond() {
        Stream<String> stream1 = Stream.of("a", "b", "c", "d", "e", "f");
        Stream<String> stream2 = Stream.of();
        Stream<String> res = interleave(stream1, stream2);
        Object[] expected = new String[]{"a", "b", "c", "d", "e", "f"};
        Object[] actual = res.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIntersection_smallerFirst() {
        Stream<Integer> stream1 = Stream.of(1, 2, 2, 4);
        Stream<Integer> stream2 = Stream.of(1, 2, 2, 2, 3, 4);
        Stream<Integer> res = intersection(stream1, stream2);
        Object[] expected = new Integer[]{1, 2, 4};
        Object[] actual = res.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIntersection_smallerSecond() {
        Stream<Integer> stream1 = Stream.of(1, 2, 2, 2, 3, 4);
        Stream<Integer> stream2 = Stream.of(1, 2, 2, 4);
        Stream<Integer> res = intersection(stream1, stream2);
        Object[] expected = new Integer[]{1, 2, 4};
        Object[] actual = res.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIntersection_noElemsFirst() {
        Stream<Integer> stream1 = Stream.of();
        Stream<Integer> stream2 = Stream.of(1, 2, 2, 2, 3, 4);
        Stream<Integer> res = intersection(stream1, stream2);
        Object[] expected = new Integer[]{};
        Object[] actual = res.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIntersection_noElemsSecond() {
        Stream<Integer> stream1 = Stream.of(1, 2, 2, 2, 3, 4);
        Stream<Integer> stream2 = Stream.of();
        Stream<Integer> res = intersection(stream1, stream2);
        Object[] expected = new Integer[]{};
        Object[] actual = res.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testPrefix() {
        Stream<String> headers = Stream.of("A:", "B:", "C:");
        Stream<Stream<String>> content = Stream.of(
                Stream.of("1", "2", "3"),
                Stream.of("4", "5", "6"),
                Stream.of("7", "8", "9")
        );
        Stream<String> res = prefix(headers, content);
        Object[] expected = new String[]{
                "A:", "1", "2", "3",
                "B:", "4", "5", "6",
                "C:", "7", "8", "9",
        };
        Object[] actual = res.toArray();

        System.out.println(Arrays.toString(actual));

        assertArrayEquals(expected, actual);
    }
}
