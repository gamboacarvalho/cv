package org.isel.jingle.util;

import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class ObservablesUtil {
    public static <T, U, R> Observable<R> merge(Observable<T> first, Observable<U> second,
                                                BiPredicate<T, U> predicate, BiFunction<T, U, R> merger, U defaultValue) {

        return first.flatMapSingle(item -> {
            Single<U> value = second
                    .filter(u -> predicate.test(item, u))   //Filter for <U> elements that fit the given predicate
                    .first(defaultValue);                   //Only the first element that fulfils given condition is needed, or default (if none)


            //Combines both elements with given merger, without needing to call blocking methods
            //Such as 'value.blockingFirst()'
            return Single.zip(Single.just(item), value, merger::apply);
        });
    }
}
