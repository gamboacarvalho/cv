package org.isel.jingle.util.queries;

import org.isel.jingle.util.iterators.*;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class LazyQueries<T> implements Queries<T> {
    private final Iterable<T> src;

    private LazyQueries(Iterable<T> src) {
        this.src = src;
    }

    public static <T> LazyQueries<T> of(Iterable<T> src) {
        return new LazyQueries<>(src);
    }


    @Override
    public Queries<T> filter(Predicate<T> filter) {
        return new LazyQueries<>(
                () -> new IteratorFilter<>(src, filter)
        );
    }

    @Override
    public Queries<T> skip(int nr) {
        return new LazyQueries<>(
                () -> {
                    Iterator<T> iter = src.iterator();
                    int count = nr;
                    while (count-- > 0 && iter.hasNext()) iter.next();
                    return iter;
                }
        );
    }

    @Override
    public Queries<T> limit(int nr) {
        return new LazyQueries<>(
                () -> new IteratorLimit<>(src, nr)
        );
    }

    @Override
    public <R> Queries<R> map(Function<T, R> map) {
        return new LazyQueries<>(
                () -> new IteratorMap<>(src, map)
        );
    }

    @Override
    public Queries<T> generate(Supplier<T> next) {
        return new LazyQueries<>(
                () -> new IteratorGenerator<>(next)
        );
    }

    @Override
    public Queries<T> iterate(T seed, Function<T, T> next) {
        return new LazyQueries<>(
                () -> new InfiniteIterator<>(seed, next)
        );
    }

    @Override
    public int count() {
        return new CountIterator<>(src).count();
    }

    @Override
    public Object[] toArray() {
        LinkedList<T> res = new LinkedList<>();
        for (T item : src) res.add(item);
        return res.toArray();
    }

    @Override
    public Optional<T> first() {
        Iterator<T> iter = src.iterator();
        return iter.hasNext() ? Optional.ofNullable(iter.next()) : Optional.empty();
    }

    @Override
    public Optional<T> max(Comparator<? super T> comparator) {
        Iterator<T> iter = src.iterator();
        T res = iter.next();
        while (iter.hasNext()) {
            T curr = iter.next();
            if (comparator.compare(curr, res) > 0)
                res = curr;
        }
        return Optional.of(res);
    }

    @Override
    public Queries<T> from(T[] items) {
        return new LazyQueries<>(
                () -> new IteratorFrom<>(items)
        );
    }

    @Override
    public Queries<T> takeWhile(Predicate<T> predicate) {
        return new LazyQueries<>(
                () -> new IteratorTakeWhile<>(src, predicate)
        );
    }

    @Override
    public <R> Queries<R> flatMap(Function<T, Iterable<R>> mapper) {
        return new LazyQueries<>(
                () -> new FlatMapIterator<>(src, mapper)
        );
    }

    @Override
    public T last() {
        return new LastIterator<>(src).getLast();
    }

    @Override
    public Queries<T> cache() {
        //Supplier needs to supply the same instance of cache and source Iterator
        //If this doesn't happen, then:
        //  -the cache is emptied on every new cache iteration
        //  -a new iterator is supplier, resetting initial values
        LinkedList<T> cache = new LinkedList<>();
        Iterator<T> srcIterator = src.iterator();
        return new LazyQueries<>(
                () -> new CacheIterator<>(srcIterator, cache)
        );
    }

    @Override
    public Iterator<T> iterator() {
        return src.iterator();
    }
}
