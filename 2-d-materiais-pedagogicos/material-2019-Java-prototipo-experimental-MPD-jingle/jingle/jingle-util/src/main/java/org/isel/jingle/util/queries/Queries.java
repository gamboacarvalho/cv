package org.isel.jingle.util.queries;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Queries<T> extends Iterable<T> {
    Queries<T> filter(Predicate<T> filter);

    Queries<T> skip(int nr);

    Queries<T> limit(int nr);

    <R> Queries<R> map(Function<T, R> map);

    Queries<T> generate(Supplier<T> next);

    Queries<T> iterate(T seed, Function<T, T> next);

    int count();

    Object[] toArray();

    Optional<T> first();

    Optional<T> max(Comparator<? super T> comparator);

    Queries<T> from(T[] items);

    Queries<T> takeWhile(Predicate<T> predicate);

    <R> Queries<R> flatMap(Function<T, Iterable<R>> mapper);

    T last();

    Queries<T> cache();
}

