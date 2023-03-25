package util;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

/**
 * Auxiliary class with utility methods for streams.
 */
public class Queries {

    /**
     * Transverse the two streams passed applying the function applyEquals if pred was satisfied or applying applyToFirst if
     * was not. When pred is satisfied we remove the element from the second stream. After the transverse of first
     * stream we go transverse the second (without duplicates) applying applyToScnd function on each element.
     */
    public static <T,U,R> Stream<R> mapDistinct(Stream<T> first, Stream<U> scnd , BiPredicate<T,U> pred,Function<T,R> applyToFirst,
                                             Function<U,R> applyToScnd, BiFunction<T,U,R> applyEquals) {
        return stream(new mapDistinctSpliterator<>(first,scnd,pred,applyToFirst,applyToScnd,applyEquals),false);
    }

    private static class mapDistinctSpliterator<T,U,R> extends Spliterators.AbstractSpliterator<R> {

        private final List<U> scnd;
        private final BiPredicate<T, U> pred;
        private final BiFunction<T, U, R> applyEquals;
        private final Function<U, R> applyToScnd;
        private final Function<T, R> applyToFirst;
        private final Spliterator<T> firstSpliterator;
        private Spliterator<U> scndSpliterator;

        mapDistinctSpliterator(Stream<T> first, Stream<U> scnd, BiPredicate<T, U> pred, Function<T, R> applyToFirst,
                                      Function<U, R> applyToScnd, BiFunction<T, U, R> applyEquals) {
            super(Long.MAX_VALUE, DISTINCT | NONNULL);
            this.firstSpliterator = first.spliterator();
            this.scnd = scnd.collect(Collectors.toList());
            this.pred = pred;
            this.applyToFirst = applyToFirst;
            this.applyToScnd = applyToScnd;
            this.applyEquals = applyEquals;
        }

        @Override
        public boolean tryAdvance(Consumer<? super R> action) {
            if (firstSpliterator.tryAdvance((item) -> action.accept(checkIfEquals(item)))){
                return true;
            }
            else if ( scndSpliterator == null ){
                scndSpliterator = scnd.spliterator();
            }

            return scndSpliterator.tryAdvance( (item) -> action.accept(applyToScnd.apply(item)));
        }

        private R checkIfEquals(T item){
            for (U it : scnd){
                if ( pred.test(item,it) ){
                    scnd.remove(it);
                    return applyEquals.apply(item,it);
                }
            }
            return applyToFirst.apply(item);
        }
    }
}
