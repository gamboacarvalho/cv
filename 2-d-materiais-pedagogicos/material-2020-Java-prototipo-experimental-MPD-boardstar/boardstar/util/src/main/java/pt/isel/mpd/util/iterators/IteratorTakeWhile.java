package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class IteratorTakeWhile<T> implements Iterator<T> {
    Iterator<T> iter;
    Predicate<T> pred;
    T curr;
    boolean called = false;
    boolean shouldStop = false;

    public IteratorTakeWhile(Iterable<T> src, Predicate<T> pred) {
        this.iter = src.iterator();
        this.pred = pred;
    }

    @Override
    public boolean hasNext() {
        if(called) return true;
        while (iter.hasNext() && !shouldStop) {
            curr = iter.next();
            shouldStop = !pred.test(curr);
            if (!shouldStop) {
                called = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        called = false;
        return curr;
    }
}
