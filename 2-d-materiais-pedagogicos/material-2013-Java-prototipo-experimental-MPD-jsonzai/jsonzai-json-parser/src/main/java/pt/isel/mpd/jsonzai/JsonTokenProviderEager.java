package pt.isel.mpd.jsonzai;

import pt.isel.mpd.jsonzai.JsonTokenSpliterator.ToListCollector;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class JsonTokenProviderEager {
    String src;
    int idx = 0;
    Iterator<JsonTokenPosition> nextToken;

    private void skipSpaces() {

        while(idx < src.length() && Character.isSpaceChar(src.charAt(idx))) {
            ++idx;
        }
    }

    public void setString(String src, boolean parallel) {
        idx = 0;
        this.src = src;
        this.nextToken = stream(new JsonTokenSpliterator(src), parallel)
                .collect(new ToListCollector<JsonTokenPosition>())
                .iterator();
    }

    public JsonToken getNextToken(){
        while(nextToken.hasNext()){
            skipSpaces();
            JsonTokenPosition pos = nextToken.next();
            if(pos.idx < idx) continue;
            /**
             * Apparently we are repeating the idx on next call.
             * We could captured the idx value when we created the lambda, however
             * that would promote several resolutions of the invokedynamic to different
             * call sites capturing different values for the dynamic argument idx.
             * To avoid that overhead and take advantage of the performance of subsequent
             * calls we prefered a Function instead of a Supplier.
             */
            JsonToken tk = pos.tokenProvider.apply(idx + 1, src);
            idx += tk.getLength();
            return tk;
        }
        throw new Error("No more tokens available!");
    }
}
