package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;

public class IteratorTakeWhile<T> implements Iterator<T> {
    private final Iterator<T> src;
    private final Predicate<T> pred;

    private boolean canContinue = true;
    private boolean end;
    private Optional<T> next = Optional.empty();

    public IteratorTakeWhile(Iterable<T> src, Predicate<T> pred) {
        this.src = src.iterator();
        this.pred = pred;
    }


    @Override
    public boolean hasNext() {
        if (!canContinue) {
            return false;
        }

        if (next.isPresent()) {
            return true;
        }

        if (src.hasNext()) {
            T item = src.next();
            canContinue = pred.test(item);

            if (canContinue) {
                this.next = Optional.ofNullable(item);
            }
            return canContinue;
        }
        canContinue = false;
        return false;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        T aux = next.get();
        next = Optional.empty();
        return aux;
    }
}
