package org.isel.jingle.util.iterators;

import java.util.Iterator;

public class IteratorFrom<T> implements Iterator<T> {
    private final T[] items;
    private int curr = 0;

    public IteratorFrom(T[] items) {
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        return items.length > curr;
    }

    @Override
    public T next() {
        return items[curr++];
    }
}
