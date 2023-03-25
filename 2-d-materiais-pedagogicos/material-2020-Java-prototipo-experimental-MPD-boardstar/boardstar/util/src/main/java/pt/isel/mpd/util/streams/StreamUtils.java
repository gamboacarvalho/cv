package pt.isel.mpd.util.streams;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    public static <T> Supplier<Stream<T>> cache(Stream<T> src) {
        return new Supplier<>() {
            private final Spliterator<T> srcSpliterator = src.spliterator();
            private final ArrayList<T> cachedElements = new ArrayList<>();

            @Override
            public Stream<T> get() {
                return StreamSupport.stream(new CacheSpliterator<>(srcSpliterator, cachedElements), false);
            }
        };
    }

    public static <T> Supplier<Stream<T>> cache(Supplier<Stream<T>> src) {
        return new Supplier<>() {
            private Supplier<Stream<T>> supplier = src;
            private Spliterator<T> srcSpliterator = null;
            private final ArrayList<T> cachedElements = new ArrayList<>();

            @Override
            public Stream<T> get() {
                if(srcSpliterator == null) {
                    srcSpliterator = supplier.get().spliterator();
                }
                return StreamSupport.stream(new CacheSpliterator<>(srcSpliterator, cachedElements), false);
            }
        };
    }

    public static <T> Stream<T> interleave(Stream<T> src, Stream<T> other) {
        Spliterator<T> srcIt = src.spliterator();
        Spliterator<T> otherIt = other.spliterator();

        return StreamSupport.stream(
                new Spliterators.AbstractSpliterator<T>(srcIt.estimateSize() + otherIt.estimateSize(), srcIt.characteristics() & otherIt.characteristics()) {
                    private boolean switcher = false;

                    @Override
                    public boolean tryAdvance(Consumer<? super T> action) {
                        if(switcher) {
                            switcher = false;
                            return otherIt.tryAdvance(action) || srcIt.tryAdvance(action);
                        }
                        switcher = true;
                        return srcIt.tryAdvance(action) || otherIt.tryAdvance(action);
                    }
        }, false);
    }

    public static <T> Stream<T> intersection(Stream<T> src, Stream<T> other) {
        Supplier<Stream<T>> otherSupplier = cache(other.distinct());
        return src.distinct().filter(
                (item) -> otherSupplier.get().anyMatch((otherItem) -> otherItem.equals(item))
        );
    }

    public static <T> Stream<T> prefix(Stream<T> headers, Stream<Stream<T>> content) {
        return interleave(headers.map(Stream::of), content).flatMap(stream -> stream);
    }
}
