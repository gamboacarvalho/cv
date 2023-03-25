package pt.isel.mpd.util.iterators;

import java.util.Iterator;
import java.util.function.UnaryOperator;

public class IteratorIterate <T> implements Iterator<T> {
    private T seed;
    private final UnaryOperator<T> acc;

    public IteratorIterate(T seed, UnaryOperator<T> acc) {
        this.seed = seed;
        this.acc = acc;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T next() {
        T res = seed;
        seed = acc.apply(seed);
        return res;
    }
}
