package org.isel.jingle.util.spliterators;

import org.isel.jingle.util.streams.StreamUtils;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class MergeSpliterator<T, U, R> extends Spliterators.AbstractSpliterator<R> {
    private final Spliterator<T> first;
    private final Supplier<Stream<U>> second;
    private final BiPredicate<T, U> predicate;
    private final BiFunction<T, U, R> merger;
    private final U defaultValue;

    public MergeSpliterator(Spliterator<T> first, Stream<U> second, BiPredicate<T, U> predicate, BiFunction<T, U, R> merger, U defaultValue) {
        super(first.estimateSize(), first.characteristics());
        this.first = first;
        this.second = StreamUtils.of(second).cache();   //Value is cached because it is operated various times
        this.predicate = predicate;
        this.merger = merger;
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        return first.tryAdvance(item -> {
            U mergeValue = second
                    .get()
                    .filter(u -> predicate.test(item, u))   //Filter for <U> elements that fit the given predicate
                    .findFirst()                            //Only the first element that fulfils given conditions is needed
                    .orElse(defaultValue);                  //If no value is found, then return default value

            R result = merger.apply(item, mergeValue);
            action.accept(result);
        });
    }
}
