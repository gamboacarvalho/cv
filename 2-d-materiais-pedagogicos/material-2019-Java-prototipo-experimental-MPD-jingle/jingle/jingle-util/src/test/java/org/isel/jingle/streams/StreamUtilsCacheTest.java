package org.isel.jingle.streams;

import org.isel.jingle.util.streams.StreamUtils;
import org.junit.Test;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StreamUtilsCacheTest {

    @Test
    public void cacheForIndividuallyIteration() {
        int max = 10;
        Stream<Double> values = Stream.generate(Math::random).limit(max);
        Supplier<Stream<Double>> cached = StreamUtils.of(values).cache();

        Iterator<Double> iter1 = cached.get().iterator();
        Iterator<Double> iter2 = cached.get().iterator();
        assertEquals(iter1.next(), iter2.next());
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

    @Test
    public void cachingStreamShouldNotIterateSourceElements() {
        final int count[] = {0};
        final int expectedMax = 10;

        Stream<Integer> actual = Stream
                .iterate(1, nr -> nr++)
                .limit(expectedMax)
                .map((nr) -> nr + ++count[0]);

        Supplier<Stream<Integer>> cached = StreamUtils.of(actual).cache();

        assertEquals(0, count[0]);
        assertEquals(expectedMax, cached.get().count());
        assertEquals(expectedMax, count[0]);
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test(expected = IllegalStateException.class)
    public void cachingShouldCloseSourceStream() {
        Stream<Integer> values = Stream.of(0, 1, 2, 3, 4);

        Supplier<Stream<Integer>> cached = StreamUtils.of(values).cache();

        //operations on cache are fine, but not on src
        cached.get().findFirst();

        values.findFirst();
        fail();
    }

    @Test(expected = IllegalStateException.class)
    public void cachingStreamTwiceShouldThrowException() {
        Stream<Integer> values = Stream.of(0, 1, 2, 3, 4);

        StreamUtils<Integer> of = StreamUtils.of(values);
        of.cache();
        of.cache();
        fail();
    }
}
