package util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Cache {

    public static <T> Supplier<Stream<T>> of(Supplier<Stream<T>> dataSrc) {

        final Recorder<T> rec = new Recorder<>(dataSrc);
        return () -> {
            // CacheIterator starts on index 0 and reads data from src or
            // from an internal cache of Recorder.
            Spliterator<T> iter = rec.cacheIterator();
            return StreamSupport.stream(iter, false);
        };
    }

    static class Recorder<T> {
        Spliterator<T> src;
        final List<T> cache = new ArrayList<>();
        final Supplier<Stream<T>> data;
        boolean hasNext = true;

        public Recorder(Supplier<Stream<T>> data) {
            this.data = data;
        }

        private synchronized boolean getOrAdvance(
                final int index,
                Consumer<? super T> cons) {
            if (index < cache.size()) {
                // If it is in cache then just get if from the corresponding index.
                cons.accept(cache.get(index));
                return true;
            } else if (hasNext)
                // If not in cache then advance the src iterator
                hasNext = src.tryAdvance(item -> {
                    cache.add(item);
                    cons.accept(item);
                });
            return hasNext;
        }

        public Spliterator<T> cacheIterator() {
            affectSource(); //affect the source spliterator with the provided stream spliterator
            return new Spliterators.AbstractSpliterator<T>(
                    src.estimateSize(), src.characteristics()
            ) {
                int index = 0;
                public boolean tryAdvance(Consumer<? super T> cons) {
                    return getOrAdvance(index++, cons);
                }
                public Comparator<? super T> getComparator() {
                    return src.getComparator();
                }
            };
        }

        private void affectSource(){
            if (src == null)
                this.src = data.get().spliterator();
        }
    }
}
