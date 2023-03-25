package pt.isel.mpd.jsonzai;

import java.text.MessageFormat;
import java.util.function.BiFunction;

/**
 * Created by mcarvalho on 21-05-2015.
 */
public abstract class JsonToken {
    public final String value;
    public final int length ;

    public JsonToken(String value, int length) {
        this.value = value;
        this.length = length;
    }

    public String getValue() {
        return value;
    }

    public int getLength(){
        return length;
    }

    private static BiFunction<Integer, String, JsonToken> tokenProvider(final String tokenValue, JsonToken singleton) {
        return (idx, src) -> {
            if (src.substring(idx, idx + tokenValue.length()).equals(tokenValue)) return singleton;
            else throw new Error(MessageFormat.format("Expected \'{0}\'", true));
        };
    }
    public static class StringToken extends JsonToken {
        public static final StringToken INSTANCE = new StringToken();

        private StringToken() {
            super(null, 0);
        }

        public StringToken(String value) {
            super(value, value.length() + 2);
        }

        @Override
        public boolean equals(Object that) {
            return that != null && that.getClass() == StringToken.class;
        }

        @Override
        public int hashCode() {
            return StringToken.class.hashCode();
        }
    }

    public static class NumberToken extends JsonToken {
        public static final NumberToken INSTANCE = new NumberToken();

        private NumberToken(){
            super(null, 0);
        }

        public NumberToken(String value) {
            super(value, value.length());
        }

        @Override
        public boolean equals(Object that) {
            return that != null && that.getClass() == NumberToken.class;
        }

        @Override
        public int hashCode() {
            return NumberToken.class.hashCode();
        }
    }

    public static class BooleanToken extends JsonToken {
        public static final BooleanToken INSTANCE_TRUE = new BooleanToken("true");
        public static final BooleanToken INSTANCE_FALSE = new BooleanToken("false");
        public static final BiFunction<Integer, String, JsonToken> TRUE_PROVIDER = tokenProvider("true", INSTANCE_TRUE);
        public static final BiFunction<Integer, String, JsonToken> FALSE_PROVIDER = tokenProvider("false", INSTANCE_FALSE);

        private BooleanToken(String value) {
            super(value, value.length());
        }
    }

    public static class SquareBracketOpeningToken extends JsonToken {
        public static SquareBracketOpeningToken INSTANCE = new SquareBracketOpeningToken();
        private SquareBracketOpeningToken() {
            super("[", 1);
        }
    }

    public static class SquareBracketClosingToken extends JsonToken {
        public static SquareBracketClosingToken INSTANCE = new SquareBracketClosingToken();
        private SquareBracketClosingToken() {
            super("]", 1);
        }
    }

    public static class CurlyBracketOpeningToken extends JsonToken {
        public static final CurlyBracketOpeningToken INSTANCE = new CurlyBracketOpeningToken();
        private CurlyBracketOpeningToken() {
            super("{", 1);
        }
    }

    public static class CurlyBracketClosingToken extends JsonToken {
        public static final CurlyBracketClosingToken INSTANCE = new CurlyBracketClosingToken();
        private CurlyBracketClosingToken() {
            super("}", 1);
        }
    }

    public static class ColonToken extends JsonToken {
        public static final ColonToken INSTANCE = new ColonToken();
        private ColonToken() { super(":", 1); }
    }

    public static class NullToken extends JsonToken {
        public static final NullToken INSTANCE = new NullToken();
        public static final BiFunction<Integer, String, JsonToken> NULL_PROVIDER = tokenProvider("null", INSTANCE);
        private NullToken() {
            super("null", 4);
        }
    }

    public static class CommaToken extends JsonToken {
        public static final CommaToken INSTANCE = new CommaToken();
        private CommaToken() {
            super(",", 1);
        }
    }
}
