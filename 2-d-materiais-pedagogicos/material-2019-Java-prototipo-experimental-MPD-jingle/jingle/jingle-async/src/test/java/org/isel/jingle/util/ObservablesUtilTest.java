package org.isel.jingle.util;

import com.google.common.collect.Lists;
import io.reactivex.Observable;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ObservablesUtilTest {
    //Values given to us
    private final List<String> seq1 = Lists.newArrayList("isel", "ola", "dup", "super", "jingle");
    private final List<Integer> seq2Asc = Lists.newArrayList(4, 5, 6, 7);
    private final List<Integer> seq2AscDupl = Lists.newArrayList(4, 5, 6, 7, 4, 5, 6, 7);
    private final List<Integer> seq2Desc = Lists.newArrayList(7, 6, 5, 4);
    private final List<Integer> seq2DescDupl = Lists.newArrayList(7, 6, 5, 4, 7, 6, 5, 4);

    //Aux. values made by us
    private static final List<String> seq = Lists.newArrayList("isel", "ola", "dup", "super", "jingle");
    private static final List<String> expected = Lists.newArrayList("isel4", "ola0", "dup0", "super5", "jingle6");
    private static final List<Integer> numbers = Lists.newArrayList(4, 5, 6, 7);
    private static final List<Integer> numbers2 = Lists.newArrayList(7, 4, 5, 6);


    @Test
    public void mergeWithSameCompareElementsButDifferentOrderShouldReturnEqualResults() {
        Observable<String> first = Observable.fromIterable(seq);
        Observable<Integer> second = Observable.fromIterable(numbers);

        //First merge
        Observable<String> actual1 = ObservablesUtil.merge(first, second, this::lengthEquals, this::concat, 0);

        //Second merge, same first String Observable but with another Integer Observable with same values but in a different order
        second = Observable.fromIterable(numbers2);
        Observable<String> actual2 = ObservablesUtil.merge(first, second, this::lengthEquals, this::concat, 0);

        //
        //Expected the result to be the same
        //
        List<String> result1 = actual1.toList().blockingGet();
        List<String> result2 = actual2.toList().blockingGet();

        assertEquals(expected, result1);
        assertEquals(expected, result2);
        assertEquals(result1, result2);
    }


    @Test
    public void mergeOnEmptySecondStreamShouldMergeAllItemsWithDefaultValue() {
        Observable<String> first = Observable.fromIterable(seq);
        Observable<Integer> empty = Observable.empty();

        List<String> expected = first.map(str -> str + 0).toList().blockingGet();

        //Merge with second Observable empty
        Observable<String> actual = ObservablesUtil.merge(first, empty, this::lengthEquals, this::concat, 0);

        //Result should be all elements of first, but with the default value applied to all
        assertEquals(expected, actual.toList().blockingGet());
    }

    @Test
    public void mergeOnEmptySourceObservableShouldReturnSameObservable() {
        Observable<String> empty = Observable.empty();
        Observable<Integer> second = Observable.fromIterable(numbers2);

        List<String> expected = empty.toList().blockingGet();

        //Merge with second Observable empty
        Observable<String> actual = ObservablesUtil.merge(empty, second, this::lengthEquals, this::concat, 0);

        //Result should be the same as source: an empty observable
        assertTrue(actual.isEmpty().blockingGet());
        assertEquals(expected, actual.toList().blockingGet());
    }

    //-------------------------------------------------------------------------
    //Set of tests given to us
    @Test
    public void shouldMergeSequencesWithoutDuplicatesOnSeq2Ascending() {
        final List<String> merged = merge(seq2Asc);

        assertEquals(expected, merged);
    }

    @Test
    public void shouldMergeSequencesWithoutDuplicatesOnSeq2AscendingWithDuplicates() {
        final List<String> merged = merge(seq2AscDupl);
        assertEquals(expected, merged);
    }

    @Test
    public void shouldMergeSequencesWithDuplicatesOnSeq2Descending() {
        final List<String> merged = merge(seq2Desc);

        assertEquals(expected, merged);
    }

    @Test
    public void shouldMergeSequencesWithDuplicatesOnSeq2DescendingWithDuplicates() {
        final List<String> merged = merge(seq2DescDupl);

        assertEquals(expected, merged);
    }

    @Test
    public void shouldMergeSequencesWithDuplicatesOnBothSequences() {
        final List<String> merged = merge(
                Stream.concat(seq1.stream(), seq1.stream()).collect(toList()),
                seq2DescDupl);

        assertEquals(Stream.concat(expected.stream(), expected.stream()).collect(toList()), merged);
    }


    //-------------------------------------------------------------------------
    //Auxiliary methods
    private boolean lengthEquals(String str, Integer value) {
        return str.length() == value;
    }

    private String concat(String str, Integer value) {
        return str + value;
    }

    private List<String> merge(List<Integer> seq2) {
        return ObservablesUtil
                .merge(Observable.fromIterable(seq1), Observable.fromIterable(seq2), this::lengthEquals, this::concat, 0)
                .toList()
                .blockingGet();
    }

    private List<String> merge(List<String> seq1, List<Integer> seq2) {
        return ObservablesUtil
                .merge(Observable.fromIterable(seq1), Observable.fromIterable(seq2), this::lengthEquals, this::concat, 0)
                .toList()
                .blockingGet();
    }
}
