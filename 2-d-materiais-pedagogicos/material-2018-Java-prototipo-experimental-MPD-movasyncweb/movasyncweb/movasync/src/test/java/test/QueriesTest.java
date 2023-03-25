package test;

import org.junit.jupiter.api.Test;
import util.Queries;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class QueriesTest {

    @Test
    public void mapDistinctTest() {
        Stream<Integer> f = Stream.of(7,9,11,12,20);
        Stream<Integer> s = Stream.of(5,12,10,11,8);
        BiPredicate<Integer,Integer> biPred = Integer::equals;
        Function<Integer,Integer> applyToFirst = (it1) -> it1 + 100;
        Function<Integer,Integer> applyToScnd = (it1) -> it1;
        BiFunction<Integer,Integer,Integer> applyToBoth = (it1, it2) -> it1 + 200;
        Iterable<Integer> expected = Arrays.asList(107, 109, 211, 212, 120, 5, 10, 8);
        Iterable<Integer> result = () -> Queries.mapDistinct(f, s, biPred, applyToFirst, applyToScnd, applyToBoth).iterator();
        assertIterableEquals(expected,result);
    }

    @Test
    public void mapDistinctEmptyFirstStreamTest() {
        Stream<Integer> f = Stream.of();
        Stream<Integer> s = Stream.of(5,12,10,11,8);
        BiPredicate<Integer,Integer> biPred = Integer::equals;
        Function<Integer,Integer> applyToFirst = (it1) -> it1 + 100;
        Function<Integer,Integer> applyToScnd = (it1) -> it1;
        BiFunction<Integer,Integer,Integer> applyToBoth = (it1, it2) -> it1 + 200;
        Iterable<Integer> expected = Arrays.asList(5,12,10,11,8);
        Iterable<Integer> result = () -> Queries.mapDistinct(f, s, biPred, applyToFirst, applyToScnd, applyToBoth).iterator();
        assertIterableEquals(expected,result);
    }

    @Test
    public void mapDistinctEmptyStreamTest() {
        Stream<Integer> f = Stream.of();
        Stream<Integer> s = Stream.of();
        BiPredicate<Integer,Integer> biPred = Integer::equals;
        Function<Integer,Integer> applyToFirst = (it1) -> it1 + 100;
        Function<Integer,Integer> applyToScnd = (it1) -> it1;
        BiFunction<Integer,Integer,Integer> applyToBoth = (it1, it2) -> it1 + 200;
        Iterable<Integer> expected = Arrays.asList();
        Iterable<Integer> result = () -> Queries.mapDistinct(f, s, biPred, applyToFirst, applyToScnd, applyToBoth).iterator();
        assertIterableEquals(expected,result);
    }


}
