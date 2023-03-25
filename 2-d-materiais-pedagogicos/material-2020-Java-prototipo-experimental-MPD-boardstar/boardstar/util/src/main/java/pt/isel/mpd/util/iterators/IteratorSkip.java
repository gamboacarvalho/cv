package pt.isel.mpd.util.iterators;

import java.util.Iterator;

public class IteratorSkip<T> implements Iterator<T> {
    Iterator<T> iter;
    int n;

    public IteratorSkip(Iterable<T> src, int nr) {
        this.iter = src.iterator();
        this.n = nr;
    }

    @Override
    public boolean hasNext() {
        while(iter.hasNext() && n-- > 0){
            iter.next();
        }
        return iter.hasNext();
    }

    @Override
    public T next() {
        return iter.next();
    }
}
