package pt.isel.mpd.jsonzai;


import pt.isel.mpd.jsonzai.JsonToken.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

public class JsonTokenSpliterator implements Spliterator<JsonTokenPosition>{

    private static final int TRESHSOLD = 100;
    private final String src;
    private int lastIdx; // exclusive -- last valid index is at lastIdx - 1
    private int idx = 0;
    private JsonTokenPosition nextTokenPosition;

    public JsonTokenSpliterator(String src) {
        this(src, 0, src.length());
    }

    public JsonTokenSpliterator(String src, int idx, int lastIdx) {
        this.src = src;
        this.idx = idx;
        this.lastIdx= lastIdx;
        nextTokenPosition = getNextTokenPosition();
    }

    @Override
    public boolean tryAdvance(Consumer<? super JsonTokenPosition> action) {
        action.accept(nextTokenPosition);
        nextTokenPosition = getNextTokenPosition();
        return nextTokenPosition != null;
    }

    @Override
    public Spliterator<JsonTokenPosition> trySplit() {
        if(nextTokenPosition == null) return null;
        int currentSize = lastIdx - idx;
        if(currentSize < TRESHSOLD) return null;
        int splitPos = currentSize/2 + idx;
        int currentLastIdx = lastIdx;
        lastIdx = splitPos;
        return new JsonTokenSpliterator(src, splitPos, currentLastIdx);
    }

    @Override
    public long estimateSize() {
        return lastIdx - idx;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }

    public JsonTokenPosition getNextTokenPosition() {
        JsonTokenPosition nextTokenProvider = null;
        while(nextTokenProvider == null && idx < lastIdx) {
            skipSpaces();
            final int currIdx = idx;
            char c = src.charAt(idx++);

            if (c == '{' || c == '}') {
                nextTokenProvider = makeJsonTokenPosition(currIdx, createCurlyBracesTokenProvider(c));
            }

            if (c == '[' || c == ']') {
                nextTokenProvider = makeJsonTokenPosition(currIdx, createSquareBracesTokenProvider(c));
            }

            if (c == ':') {
                nextTokenProvider = makeJsonTokenPosition(currIdx, (idx, src) -> ColonToken.INSTANCE);
            }

            if (c == ',') {
                nextTokenProvider = makeJsonTokenPosition(currIdx, (idx, src) -> CommaToken.INSTANCE);
            }

            if (c == '"') {
                nextTokenProvider = makeJsonTokenPosition(currIdx, (_idx, _src) -> new StringToken(getString(_idx, _src)));
            }

            if (c == 't') {
                nextTokenProvider = makeJsonTokenPosition(currIdx, createTrueTokenProvider());
            }

            if (c == 'f') {
                nextTokenProvider = makeJsonTokenPosition(currIdx, createFalseTokenProvider());
            }

            if (c == 'n') {
                nextTokenProvider = makeJsonTokenPosition(currIdx, createNullTokenProvider());
            }

            if (c == '-' || Character.isDigit(c)) {
                nextTokenProvider = makeJsonTokenPosition(currIdx, (idx, src) -> new NumberToken(getNumber(idx, src)));
            }
        }
        return nextTokenProvider;
    }

    /**
     * The token provider can be null when running in parallel.
     * If this JsonTokenSpliterator was spllited, it can begins at the middle of
     * a string and every word begining with same character of a token will be
     * interpreted as a token.
     * If that interpretation succeeds there is no problem because later it will
     * be ignored by the processing of its parent string.
     * If it fails, than there was a mistake and we just ignore it returning null.
     */
    private static JsonTokenPosition makeJsonTokenPosition(int currIdx, BiFunction<Integer, String, JsonToken> tokenProvider) {
        return tokenProvider == null ? null : new JsonTokenPosition(currIdx, tokenProvider);
    }

    private void skipSpaces() {
        while(idx < lastIdx && Character.isSpaceChar(src.charAt(idx))) {
            ++idx;
        }
    }

    private BiFunction<Integer, String, JsonToken> createTrueTokenProvider() {
        final int TRUE_LENGTH = "true".length();
        if ((idx + TRUE_LENGTH) > lastIdx) return BooleanToken.TRUE_PROVIDER;
        else return checkSubstringAndUpdateIndex("true") ? (i, s) -> BooleanToken.INSTANCE_TRUE : null;
    }

    private BiFunction<Integer, String, JsonToken> createFalseTokenProvider() {
        final int FALSE_LENGTH = "false".length();
        if ((idx + FALSE_LENGTH) > lastIdx) return BooleanToken.FALSE_PROVIDER;
        else return checkSubstringAndUpdateIndex("false")? (i, s) -> BooleanToken.INSTANCE_FALSE : null;
    }

    private BiFunction<Integer, String, JsonToken> createNullTokenProvider() {
        final int NULL_LENGTH = "null".length();
        if ((idx + NULL_LENGTH) > lastIdx) return NullToken.NULL_PROVIDER;
        return checkSubstringAndUpdateIndex("null")? (i, s) -> NullToken.INSTANCE : null;
    }

    public BiFunction<Integer, String, JsonToken> createCurlyBracesTokenProvider(char c) {
        return c == '{' ?
                (i, s) -> JsonToken.CurlyBracketOpeningToken.INSTANCE :
                (i, s) -> JsonToken.CurlyBracketClosingToken.INSTANCE;
    }

    private BiFunction<Integer, String, JsonToken> createSquareBracesTokenProvider(char c) {
        return c == '[' ?
                (i, s) -> JsonToken.SquareBracketOpeningToken.INSTANCE :
                (i, s) -> JsonToken.SquareBracketClosingToken.INSTANCE;
    }


    private static String getString(int idx, String src) {
        int endIdx = idx;
        while(endIdx < src.length()) {
            final char c = src.charAt(endIdx++);
            if(c == '"') break;
            if(c == '\\') ++endIdx;
        }
        final String str = src.substring(idx, endIdx - 1);
        ++idx;
        return str;
    }

    private static String getNumber(int idx, String src) {
        int endIdx = idx--;
        while(endIdx < src.length() && Character.isDigit(src.charAt(endIdx)))
            ++endIdx;
        if(endIdx < src.length() && src.charAt(endIdx) == '.') {
            ++endIdx;
            while(endIdx < src.length() && Character.isDigit(src.charAt(endIdx)))
                ++endIdx;
        }
        return src.substring(idx, endIdx);
    }

    private boolean checkSubstringAndUpdateIndex(String str){
        --idx;
        if(src.substring(idx, idx+str.length()).equals(str)) {
            getSubstringAndUpdateIdx(idx+str.length());
            return true;
        }
        else {
            idx++;
            return false;
        }
    }

    private String getSubstringAndUpdateIdx(int endIdx) {
        String s = src.substring(idx, endIdx);
        idx = endIdx;
        skipSpaces();
        return s;
    }

    public static class ToListCollector<T> implements Collector<T, List<T>, List<T>> {
        public Supplier<List<T>> supplier(){
            return ArrayList::new;
        }
        public BiConsumer<List<T>, T> accumulator() {
            return List::add;
        }
        public BinaryOperator<List<T>> combiner() {
            return (prev, curr) -> {curr.addAll(prev); return curr;};
        }
        public Function<List<T>, List<T>> finisher() {
            return Function.identity();
        }
        public Set<Characteristics> characteristics(){
            HashSet<Characteristics> res = new HashSet<>();
            res.add(Characteristics.CONCURRENT);
            res.add(Characteristics.IDENTITY_FINISH);
            return res;
        }
    }
}
