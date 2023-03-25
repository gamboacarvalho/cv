package org.isel.jingle.util.streams;

import org.isel.jingle.util.spliterators.CacheSpliterator;
import org.isel.jingle.util.spliterators.MergeSpliterator;

import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils<T> {
    private final Stream<T> src;
    private boolean cached = false;

    private StreamUtils(Stream<T> src) {
        this.src = src;
    }

    public static <T> StreamUtils<T> of(Stream<T> src) {
        return new StreamUtils<>(src);
    }

    /**
     * Creates a new cache Supplier for given Stream, if it was not already cached.
     * <p>
     * <p>
     * A cached supplier means that values that were "iterated" on the Stream will be inserted to memory and then read from said memory
     * when needed, which might be useful to avoid multiple Http calls, for example.
     * <p>
     * <p>
     * This method is a Terminal Operation for given source, as it calls "Spliterator"
     *
     * @return a Supplier for given stream
     */
    public Supplier<Stream<T>> cache() {
        if (cached) {
            throw new IllegalStateException("Given Stream is already cached!");
        }
        cached = true;
        CacheSpliterator<T> split = new CacheSpliterator<>(src.spliterator()); //Closes src Stream!
        return () -> Stream.concat(split.getMemory().stream(), StreamSupport.stream(split, false));
    }

    public T last() {
        return src
                .reduce((first, second) -> second)
                .orElseThrow(NoSuchElementException::new);
    }


    /**
     * Creates a new Stream by merging the source Stream with given Stream
     * <p>
     * <p>
     * This is a Terminal Operation for both the source and second Stream
     */
    public <U, R> Stream<R> merge(Stream<U> second, BiPredicate<T, U> predicate, BiFunction<T, U, R> merger, U defaultValue) {
        return StreamSupport.stream(
                new MergeSpliterator<>(src.spliterator(), second, predicate, merger, defaultValue),
                false
        );
    }
}
