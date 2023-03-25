package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;

public class FlatMapIterator<T, R> implements Iterator<R> {
    private final Function<T, Iterable<R>> map;
    private final Iterator<T> src;
    private Iterator<R> mapperIterator;

    private Optional<R> next = Optional.empty();

    public FlatMapIterator(Iterable<T> src, Function<T, Iterable<R>> map) {
        this.src = src.iterator();
        this.map = map;
    }

    @Override
    public boolean hasNext() {
        if(next.isPresent()) {
            return true;
        }
        if(mapperIterator != null && mapperIterator.hasNext()){
            next = Optional.ofNullable(mapperIterator.next());
            return true;
        }
        if(src.hasNext()){
            mapperIterator = map.apply(src.next()).iterator();
            return hasNext();
        }
        return false;
    }

    @Override
    public R next() {
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        Optional<R> toReturn = next;
        next = Optional.empty();
        return toReturn.get();
    }
}