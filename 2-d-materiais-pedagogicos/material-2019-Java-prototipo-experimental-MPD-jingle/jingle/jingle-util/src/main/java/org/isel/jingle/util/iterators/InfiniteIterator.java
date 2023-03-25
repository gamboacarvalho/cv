package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.function.Function;

public class InfiniteIterator<T> implements Iterator<T> {
    private T current;
    private boolean seedConsumed = false;
    private final Function<T, T> next;

    public InfiniteIterator(T seed, Function<T, T> next) {
        this.current = seed;
        this.next = next;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        if (seedConsumed) {
            return current = next.apply(current);
        }
        seedConsumed = true;
        return current;
    }
}
