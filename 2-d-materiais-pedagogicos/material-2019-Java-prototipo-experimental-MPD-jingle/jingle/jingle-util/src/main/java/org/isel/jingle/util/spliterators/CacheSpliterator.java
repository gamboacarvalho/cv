package org.isel.jingle.util.spliterators;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class CacheSpliterator<T> extends Spliterators.AbstractSpliterator<T> {
    private final Spliterator<T> src;
    private final List<T> memory = new ArrayList<>();

    public CacheSpliterator(Spliterator<T> src) {
        super(src.estimateSize(), src.characteristics());
        this.src = src;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return src.tryAdvance(item -> {
            memory.add(item);
            action.accept(item);
        });
    }

    public List<T> getMemory() {
        return memory;
    }
}
