package pt.isel.mpd.util.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IterableCache<T> implements Iterable<T> {

    private ArrayList<T> cache = new ArrayList<>();
    private Iterable<T> src;
    private Iterator<T> srcIter;
    private int globalPos = 0;

    public IterableCache(Iterable<T> src) {
        this.src = src;
    }

    @Override
    public Iterator<T> iterator() throws NoSuchElementException {
        if (srcIter == null) srcIter = src.iterator();

        return new Iterator<>() {
            private int currPos = 0;

            @Override
            public boolean hasNext() {
                return (currPos < globalPos) || srcIter.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                if (currPos == globalPos) {
                    cache.add(globalPos++, srcIter.next());
                }

                return cache.get(currPos++);
            }
        };
    }
}


