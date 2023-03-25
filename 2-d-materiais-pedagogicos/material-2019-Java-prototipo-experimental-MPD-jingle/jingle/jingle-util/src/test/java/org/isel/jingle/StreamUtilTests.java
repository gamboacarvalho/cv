package org.isel.jingle;

import org.isel.jingle.util.streams.StreamUtils;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class StreamUtilTests {

    @Test
    public void testLastWhenExists() {
        Integer expected = 3;
        Stream<Integer> values = Stream.of(0, 1, 2, expected);

        Integer result = StreamUtils.of(values).last();

        assertEquals(expected, result);
    }

    @Test(expected = NoSuchElementException.class)
    public void lastOnEmptyStreamShouldThrowException() {
        Stream<Object> emptyStream = Stream.empty();

        Object result = StreamUtils.of(emptyStream).last();

        fail();
    }

    @Test
    public void cacheForRandomStreamShouldReturnSameValues() {
        int max = 10;
        Stream<Double> values = Stream.generate(Math::random).limit(max);

        Supplier<Stream<Double>> cached = StreamUtils.of(values).cache();

        Object[] expected = cached.get().toArray();
        Object[] result = cached.get().toArray();

        assertArrayEquals(expected, result);
    }

    @Test
    public void getSupplierValueFromCacheMultipleTimesShouldNotCloseStream() {
        Stream<Integer> values = Stream.of(0, 1, 2, 3, 4);

        Supplier<Stream<Integer>> cached = StreamUtils.of(values).cache();

        cached.get();
        cached.get();
        cached.get();
        cached.get();
    }

    @Test(expected = IllegalStateException.class)
    public void cachingShouldCloseSourceStream() {
        Stream<Integer> values = Stream.of(0, 1, 2, 3, 4);

        Supplier<Stream<Integer>> cached = StreamUtils.of(values).cache();

        //operations on cache are fine, but not on src
        Optional<Integer> first = cached.get().findFirst();

        Optional<Integer> result = values.findFirst();
        fail();
    }

    @Test(expected = IllegalStateException.class)
    public void cachingStreamTwiceShouldThrowException(){
        Stream<Integer> values = Stream.of(0, 1, 2, 3, 4);

        StreamUtils<Integer> of = StreamUtils.of(values);
        of.cache();
        of.cache();
        fail();
    }
}
