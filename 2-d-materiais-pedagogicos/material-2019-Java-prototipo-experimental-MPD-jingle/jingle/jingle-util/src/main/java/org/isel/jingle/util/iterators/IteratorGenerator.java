package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.function.Supplier;

public class IteratorGenerator<T> implements Iterator<T> {
    private final Supplier<T> src;

    public IteratorGenerator(Supplier<T> src) {
        this.src = src;
    }

    public boolean hasNext() {
        return true;
    }

    public T next() {
        return src.get();
    }
}
