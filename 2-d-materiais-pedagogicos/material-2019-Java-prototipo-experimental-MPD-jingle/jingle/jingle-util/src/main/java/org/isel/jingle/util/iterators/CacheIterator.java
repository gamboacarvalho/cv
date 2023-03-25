package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class CacheIterator<T> implements Iterator<T> {
    private final Iterator<T> src;
    private LinkedList<T> cache;
    private boolean firstTimeIterating;

    public CacheIterator(Iterator<T> src, LinkedList<T> cache) {
        this.src = src;
        this.cache = cache;
        firstTimeIterating = cache.isEmpty();
    }


    @Override
    public boolean hasNext() {
        return (!firstTimeIterating && !cache.isEmpty()) || src.hasNext();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        T aux;
        if (firstTimeIterating) {
            aux = src.next();
            cache.add(aux);
        } else {
            aux = cache.isEmpty() ? src.next() : cache.poll();
        }
        return aux;
    }
}
