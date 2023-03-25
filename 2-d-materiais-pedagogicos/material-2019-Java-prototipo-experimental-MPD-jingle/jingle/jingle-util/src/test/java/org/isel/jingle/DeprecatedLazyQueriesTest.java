/*
 * GNU General Public License v3.0
 *
 * Copyright (c) 2019, Miguel Gamboa (gamboa.pt)
 *
 *   All rights granted under this License are granted for the term of
 * copyright on the Program, and are irrevocable provided the stated
 * conditions are met.  This License explicitly affirms your unlimited
 * permission to run the unmodified Program.  The output from running a
 * covered work is covered by this License only if the output, given its
 * content, constitutes a covered work.  This License acknowledges your
 * rights of fair use or other equivalent, as provided by copyright law.
 *
 *   You may make, run and propagate covered works that you do not
 * convey, without conditions so long as your license otherwise remains
 * in force.  You may convey covered works to others for the sole purpose
 * of having them make modifications exclusively for you, or provide you
 * with facilities for running those works, provided that you comply with
 * the terms of this License in conveying all material for which you do
 * not control copyright.  Those thus making or running the covered works
 * for you must do so exclusively on your behalf, under your direction
 * and control, on terms that prohibit them from making any copies of
 * your copyrighted material outside their relationship with you.
 *
 *   Conveying under any other circumstances is permitted solely under
 * the conditions stated below.  Sublicensing is not allowed; section 10
 * makes it unnecessary.
 *
 */

package org.isel.jingle;

import junit.framework.AssertionFailedError;
import org.isel.jingle.util.queries.DeprecatedLazyQueries;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.isel.jingle.util.queries.DeprecatedLazyQueries.*;
import static org.junit.Assert.*;


public class DeprecatedLazyQueriesTest {
    private List<String> mutableTestList;

    @Before
    public void createMutableTestList() {
        mutableTestList = new ArrayList<>();
        mutableTestList.add("isel");
        mutableTestList.add("is");
        mutableTestList.add("cool");
    }

    @Test
    public void testMax() {
        Random r = new Random();
        Iterable<Integer> nrs = limit(generate(() -> r.nextInt(100)), 10);
        max(nrs).ifPresent(out::println);
    }

    @Test
    public void testSkip() {
        List<Integer> nrs = asList(1, 2, 3, 4, 5, 6, 7, 8);
        Object[] actual = toArray(skip(nrs, 3));
        Object[] expected = {4, 5, 6, 7, 8};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testLimit() {
        Iterable<Integer> nrs = DeprecatedLazyQueries.limit(iterate(1, n -> n + 1), 11);
        assertEquals(11, count(nrs));
    }

    @Test
    public void testGenerate() {
        Random r = new Random();
        Iterable<Integer> nrs = limit(generate(() -> r.nextInt(100)), 10);
        for (int n : nrs) out.println(n);
    }

    @Test
    public void testMap() {
        List<String> words = asList("super", "isel", "ola", "fcp");
        Object[] actual = toArray(map(words, w -> w.length()));
        Object[] expected = {5, 4, 3, 3};
        assertArrayEquals(expected, actual);
    }


    @Test
    public void testFirstFilterMapNrs() throws Throwable {
        // An infinite Sequence CANNOT be converted to a Collection
        // Object[] nrs = toArray(iterate(1, n -> n + 1));

        Iterable<Integer> nrs = iterate(1, n -> n + 1);
        Optional<Integer> first = first(filter(map(nrs,
                n -> n * n),
                n -> n > 3));
        int val = first.orElseThrow(() -> {
            throw new AssertionFailedError("Max returning NO value!");
        });
        assertEquals(4, val);
    }

    //---------------------------------------
    //Created tests by us:
    @Test
    public void testFirst() {
        String expected = "isel";
        List<String> words = asList(expected, "is", "cool");
        Optional<String> first = first(words);

        assertTrue(first.isPresent());
        assertEquals(expected, first.get());
    }

    @Test
    public void testCount() {
        int expected = mutableTestList.size();

        assertEquals(expected, count(mutableTestList));
        mutableTestList.remove(--expected);
        assertEquals(expected, count(mutableTestList));
    }

    @Test
    public void testLast() {
        int last = mutableTestList.size() - 1;
        String expected = mutableTestList.get(last);

        assertEquals(expected, last(mutableTestList));
        mutableTestList.remove(--last);
        assertEquals(expected, last(mutableTestList));
    }

    @Test(expected = NoSuchElementException.class)
    public void testLastOnEmptyIterableShouldThrowException() {
        Iterable<String> empty = new ArrayList<>();
        last(empty);
        //Should never get here
        fail();
    }

    @Test
    public void testFrom() {
        String[] words = new String[]{"isel", "is", "cool"};
        Iterable<String> from = from(words);

        int curr[] = {0};
        from.forEach((word) -> assertEquals(words[curr[0]++], word));
    }

    @Test
    public void testTakeWhile() {
        List<Integer> numbers = asList(1, 1, 1, 0, 1, 1);
        Integer expectedValue = 1;
        int expectedLength = 3;

        Iterable<Integer> result = takeWhile(numbers, (nr) -> nr.equals(expectedValue));

        assertEquals(expectedLength, toArray(result).length);
        result.forEach((nr) -> assertEquals(expectedValue, nr));
    }


}