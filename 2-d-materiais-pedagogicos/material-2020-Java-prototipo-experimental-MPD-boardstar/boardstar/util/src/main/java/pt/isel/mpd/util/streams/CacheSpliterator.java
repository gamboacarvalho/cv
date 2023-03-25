package pt.isel.mpd.util.streams;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

public class CacheSpliterator<T> extends Spliterators.AbstractSpliterator<T> {

    private final Spliterator<T> src;
    private final List<T> cache;
    private int currElement = 0;

    protected CacheSpliterator(Spliterator<T> src, List<T> cache) {
        super(src.estimateSize(), src.characteristics());
        this.src = src;
        this.cache = cache;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> consumer) {
        if (currElement < cache.size()) {
            consumer.accept(cache.get(currElement++));
            return true;
        } else {
            return src.tryAdvance(
                    item -> {
                        ++currElement;
                        cache.add(item);
                        consumer.accept(item);
                    }
            );
        }
    }
}
