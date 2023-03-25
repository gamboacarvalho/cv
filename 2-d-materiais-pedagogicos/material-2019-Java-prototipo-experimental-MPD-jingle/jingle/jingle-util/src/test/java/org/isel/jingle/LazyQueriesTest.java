package org.isel.jingle;

import junit.framework.AssertionFailedError;
import org.isel.jingle.util.queries.LazyQueries;
import org.isel.jingle.util.queries.Queries;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

public class LazyQueriesTest {
    private List<String> mutableWordList;
    private List<Integer> mutableNumberList;

    @Before
    public void createMutableTestList() {
        mutableWordList = new ArrayList<>();
        mutableWordList.add("isel");
        mutableWordList.add("is");
        mutableWordList.add("cool");

        mutableNumberList = new ArrayList<>();
        mutableNumberList.add(1);
        mutableNumberList.add(1);
        mutableNumberList.add(2);
        mutableNumberList.add(10);
    }

    @Test
    public void testMax() {
        Random r = new Random();
        Iterable<Integer> nrs = new ArrayList<>();
        LazyQueries
                .of(nrs)
                .generate(() -> r.nextInt(100))
                .limit(10)
                .max(Integer::compare)
                .ifPresent(out::println);

        //Test max without random, as there is no assertion in provided test
        Optional<Integer> max = LazyQueries
                .of(mutableNumberList)
                .max(Integer::compare);

        Integer expected = 10;

        assertTrue(max.isPresent());
        assertEquals(expected, max.get());
    }

    @Test
    public void testSkip() {
        List<Integer> nrs = asList(1, 2, 3, 4, 5, 6, 7, 8);
        Object[] actual = LazyQueries
                .of(nrs)
                .skip(3)
                .toArray();

        Object[] expected = {4, 5, 6, 7, 8};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testLimit() {
        Iterable<Integer> nrs = new ArrayList<>();
        int expected = 11;

        Queries<Integer> actual = LazyQueries
                .of(nrs)
                .iterate(1, n -> n + 1)
                .limit(expected);

        assertEquals(expected, actual.count());
    }

    @Test
    public void testGenerate() {
        Random r = new Random();
        Iterable<Integer> nrs = LazyQueries.of(new ArrayList<Integer>()).generate(() -> r.nextInt(100)).limit(10);
        for (int n : nrs) out.println(n);

        //Test generate without random, as there is no assertion in provided test
        int expectedNumber = 10;
        int expectedSize = 5;

        Queries<Object> actual = LazyQueries
                .of(new ArrayList<>())
                .generate(() -> expectedNumber)
                .limit(expectedSize);

        assertEquals(expectedSize, actual.count());
        actual.forEach(value -> assertEquals(expectedNumber, value));
    }

    @Test
    public void testMap() {
        List<String> words = asList("super", "isel", "ola", "fcp");
        Object[] actual = LazyQueries.of(words).map(String::length).toArray();
        Object[] expected = {5, 4, 3, 3};
        assertArrayEquals(expected, actual);
    }


    @Test
    public void testFirstFilterMapNrs() throws Throwable {
        // An infinite Sequence CANNOT be converted to a Collection
        // Object[] nrs = toArray(iterate(1, n -> n + 1));
        Queries<Integer> nrs = LazyQueries.of(new ArrayList<Integer>()).iterate(1, n -> n + 1);
        Optional<Integer> first = nrs.map(n -> n * n).filter(n -> n > 3).first();

        int val = first.orElseThrow(() -> {
            throw new AssertionFailedError("Max returning NO value!");
        });
        assertEquals(4, val);
    }

    //---------------------------------------
    //Created tests by us:
    @Test
    public void testFirst() {
        String expected = mutableWordList.get(0);
        Optional<String> first = LazyQueries.of(mutableWordList).first();

        assertTrue(first.isPresent());
        assertEquals(expected, first.get());
    }

    @Test
    public void testCount() {
        int expected = mutableWordList.size();
        LazyQueries<String> of = LazyQueries.of(mutableWordList);
        assertEquals(expected, of.count());
        mutableWordList.remove(--expected);
        assertEquals(expected, of.count());
    }

    @Test
    public void testLast() {
        int last = mutableWordList.size() - 1;
        String expected = mutableWordList.get(last);
        LazyQueries<String> of = LazyQueries.of(mutableWordList);


        assertEquals(expected, of.last());
        mutableWordList.remove(--last);
        assertEquals(expected, of.last());
    }

    @Test(expected = NoSuchElementException.class)
    public void testLastOnEmptyIterableShouldThrowException() {
        Iterable<String> empty = new ArrayList<>();

        LazyQueries.of(empty).last();
        //Should never get here
        fail();
    }

    @Test
    public void testFrom() {
        String[] words = new String[]{"isel", "is", "cool"};
        Iterable<String> from = LazyQueries.of(new ArrayList<String>()).from(words);

        int curr[] = {0};
        from.forEach((word) -> assertEquals(words[curr[0]++], word));
    }

    @Test
    public void testTakeWhile() {
        List<Integer> numbers = asList(1, 1, 1, 0, 1, 1);
        Integer expectedValue = 1;
        int expectedLength = 3;

        LazyQueries<Integer> numbersQuery = LazyQueries.of(numbers);

        Iterable<Integer> result = numbersQuery.takeWhile((nr) -> nr.equals(expectedValue));

        assertEquals(expectedLength, LazyQueries.of(result).toArray().length);
        result.forEach((nr) -> assertEquals(expectedValue, nr));
    }


    @Test
    public void testMaxWithoutRandom() {
        Optional<Integer> max = LazyQueries
                .of(mutableNumberList)
                .max(Integer::compare);

        Integer expected = 10;

        assertTrue(max.isPresent());
        assertEquals(expected, max.get());
    }

    @Test
    public void testIterateForSeed() {
        Integer first = 1;

        Queries<Integer> numbers = LazyQueries
                .of(new ArrayList<Integer>())
                .iterate(first, n -> n + 1);

        assertEquals(first, numbers.first().get());
    }

    @Test
    public void testFlatMap() {
        int[] expected = new int[]{0};
        int[] aux = new int[]{0};
        List<Character> expectedCharList = List.of('i', 's', 'e', 'l', 'i', 's', 'c', 'o', 'o', 'l');

        mutableWordList.forEach(str -> expected[0] += str.length());

        Queries<Character> characters = LazyQueries
                .of(mutableWordList)
                .flatMap(this::toChar);

        int count = characters.count();

        assertEquals(expected[0], count);
        characters.iterator().forEachRemaining(c -> assertEquals(c, expectedCharList.get(aux[0]++)));
    }

    @Test
    public void testCacheForInfiniteRandomQuery() {
        Random r = new Random();
        Queries<Integer> cachedNrs = LazyQueries
                .of(new ArrayList<Integer>())
                .generate(r::nextInt)
                .cache();

        Object[] expected = cachedNrs.limit(10).toArray();
        Object[] actual = cachedNrs.limit(10).toArray();

        assertArrayEquals(expected, actual);
    }

    private Iterable<Character> toChar(String word) {
        return word
                .chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
    }
}
