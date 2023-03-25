package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LastIterator<T> {

    private final Iterator<T> src;

    public LastIterator(Iterable<T> src) {
        this.src = src.iterator();
    }

    public T getLast() {
        int i = 0;
        T last = null;
        while (src.hasNext()) {
            last = src.next();
        }
        if (last == null) {
            throw new NoSuchElementException();
        }
        return last;
    }

}
