package org.isel.jingle.streams;

import org.isel.jingle.util.streams.StreamUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class StreamUtilsMergeTest {
    private final List<String> seq1 = Arrays.asList("isel", "ola", "dup", "super", "jingle");
    private final List<Integer> seq2Asc = Arrays.asList(4, 5, 6, 7);
    private final List<Integer> seq2AscDupl = Arrays.asList(4, 5, 6, 7, 4, 5, 6, 7);
    private final List<Integer> seq2Desc = Arrays.asList(7, 6, 5, 4);
    private final List<Integer> seq2DescDupl = Arrays.asList(7, 6, 5, 4, 7, 6, 5, 4);
    private final List<String> expected = Arrays.asList("isel4", "ola0", "dup0", "super5", "jingle6");


    //-------------------------------------------------------------------------
    //Set of tests created by us
    @Test
    public void mergeStreamWithSameCompareElementsButDifferentOrderShouldReturnEqualResults() {
        Object[] expected = Stream.of("isel4", "ola0", "dup0", "super5", "jingle6").toArray();

        Supplier<Stream<String>> firstSequence = () -> Stream.of("isel", "ola", "dup", "super", "jingle");
        Stream<Integer> secondSequence = Stream.of(4, 5, 6, 7);

        Stream<String> result = StreamUtils
                .of(firstSequence.get())
                .merge(secondSequence, (str, nr) -> str.length() == nr, (str, nr) -> str + nr, 0);

        assertArrayEquals(expected, result.toArray());

        secondSequence = Stream.of(7, 6, 5, 4);
        result = StreamUtils
                .of(firstSequence.get())
                .merge(secondSequence, (str, nr) -> str.length() == nr, (str, nr) -> str + nr, 0);

        assertArrayEquals(expected, result.toArray());
    }

    @Test
    public void mergeStreamWithSameCompareElementsButDifferentOrderShouldReturnEqualResults2() {
        Object[] expected = Stream.of("isel4", "ola0", "dup0", "super5", "jingle6").toArray();

        Supplier<Stream<String>> firstSequence = () -> Stream.of("isel", "ola", "dup", "super", "jingle");
        Stream<Integer> secondSequence = Stream.of(4, 5, 6, 7);

        Stream<String> result = StreamUtils.of(firstSequence.get()).merge(secondSequence, (str, nr) -> str.length() == nr, (str, nr) -> str + nr, 0);

        assertArrayEquals(expected, result.toArray());

        secondSequence = Stream.of(7, 6, 5, 4);
        result = StreamUtils.of(firstSequence.get()).merge(secondSequence, (str, nr) -> str.length() == nr, (str, nr) -> str + nr, 0);

        assertArrayEquals(expected, result.toArray());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test(expected = IllegalStateException.class)
    public void mergeStreamShouldTerminateBothStreamSequences() {
        Stream<String> firstSequence = Stream.of("isel", "ola", "dup", "super", "jingle");
        Stream<Integer> secondSequence = Stream.of(4, 5, 6, 7);

        StreamUtils.of(firstSequence).merge(secondSequence, (str, nr) -> str.length() == nr, (str, nr) -> str + nr, 0);

        try {
            firstSequence.count();
        } catch (IllegalStateException e) {
            secondSequence.count();
        }
        fail();
    }

    @Test
    public void mergeOnEmptySourceStreamShouldReturnSameStream() {
        Supplier<Stream<String>> emptyStream = Stream::empty;
        Supplier<Stream<Integer>> nonEmptyStream = () -> Stream.of(0, 1, 2, 3);

        Supplier<Stream<String>> actual = () -> StreamUtils
                .of(emptyStream.get())
                .merge(nonEmptyStream.get(), (str, nr) -> str.length() == nr, (str, nr) -> str + nr, 0);

        assertNotNull(actual);
        assertEquals(0, actual.get().count());
        assertArrayEquals(new Object[0], actual.get().toArray());
    }

    @Test
    public void mergeOnEmptySecondStreamShouldMergeAllItemsWithDefaultValue() {
        Supplier<Stream<String>> emptyStream = Stream::empty;
        Supplier<Stream<Integer>> nonEmptyStream = () -> Stream.of(0, 1, 2, 3);

        Supplier<Stream<Integer>> actual = () -> StreamUtils
                .of(nonEmptyStream.get())
                .merge(emptyStream.get(), (nr, str) -> nr == str.length(), (nr, str) -> nr + str.length(), "ISEL");


        assertNotNull(actual);
        assertEquals(4, actual.get().count());
        assertArrayEquals(new Object[]{4, 5, 6, 7}, actual.get().toArray());
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
    private List<String> merge(List<Integer> seq2) {
        return StreamUtils
                .of(seq1.stream())
                .merge(seq2.stream(), (str, nr) -> str.length() == nr, (str, nr) -> str + nr, 0)
                .collect(toList());
    }

    private List<String> merge(List<String> seq1, List<Integer> seq2) {
        return StreamUtils
                .of(seq1.stream())
                .merge(seq2.stream(), (str, nr) -> str.length() == nr, (str, nr) -> str + nr, 0)
                .collect(toList());
    }


}
