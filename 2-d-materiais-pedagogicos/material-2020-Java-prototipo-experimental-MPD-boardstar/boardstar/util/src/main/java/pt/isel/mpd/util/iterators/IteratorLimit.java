package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorLimit <T> implements Iterator<T> {
    Iterator<T> iter;
    int idx = 0, nr;

    public IteratorLimit(Iterable<T> src, int nr) {
        this.iter = src.iterator();
        this.nr = nr;
    }

    @Override
    public boolean hasNext() {
        return idx < nr && iter.hasNext();
    }

    @Override
    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        idx++;
        return iter.next();
    }
}
