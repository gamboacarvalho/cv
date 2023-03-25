package org.isel.jingle.streams;

import org.isel.jingle.util.streams.StreamUtils;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StreamUtilsLastTest {
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

        StreamUtils.of(emptyStream).last();

        fail();
    }
}
