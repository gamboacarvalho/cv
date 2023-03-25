package org.isel.jingle.util.iterators;

import java.util.Iterator;

public class CountIterator<T> {

    private final Iterator<T> src;

    public CountIterator(Iterable<T> src) {
        this.src = src.iterator();
    }

    public int count() {
        int countResult = 0;
        while (src.hasNext()) {
            src.next();
            countResult++;
        }
        return countResult;
    }
}
