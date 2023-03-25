package pt.isel.mpd.util.iterators;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class IteratorFlatMap <T, R> implements Iterator<R> {
    Iterator<T> src;
    Iterator<R> curr = Collections.emptyIterator();
    Function<T, Iterable<R>> map;

    public IteratorFlatMap(Iterable<T> src, Function<T, Iterable<R>> mapper) {
        this.src = src.iterator();
        map = mapper;
        if (this.src.hasNext()) {
            curr = map.apply(this.src.next()).iterator();
        }
    }

    @Override
    public boolean hasNext() {
        if(curr.hasNext()) return true;
        while(src.hasNext()) {
            curr = map.apply(src.next()).iterator();
            if(curr.hasNext()) return true;
        }
        return false;
    }

    @Override
    public R next() {
        if(!hasNext()) {
            throw new NoSuchElementException();
        }
        return curr.next();
    }
}
