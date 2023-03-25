package pt.isel.mpd.jsonzai;

import pt.isel.mpd.jsonzai.JsonToken.BooleanToken;
import pt.isel.mpd.jsonzai.JsonToken.NumberToken;
import pt.isel.mpd.jsonzai.JsonToken.StringToken;

import java.text.MessageFormat;

import static pt.isel.mpd.jsonzai.JsonToken.*;

/**
 * Receives a Json string ad provides its Tokens each time getNextToken called.
 */
public class JsonTokenProvider {
    String jsonString;
    int idx = 0;

    public JsonTokenProvider() {
    }

    private void skipSpaces() {
        while(idx < jsonString.length() && Character.isSpaceChar(jsonString.charAt(idx))) {
            ++idx;
        }
    }

    public void setString(String jsonStr) {
        idx = 0;
        jsonString = jsonStr;
    }


    public JsonToken getNextToken() {
        skipSpaces();

        if(idx >= jsonString.length())
            throw new Error("End of json string reached!");

        char c = jsonString.charAt(idx++);

        if(c == '{' || c == '}') {
            return createCurlyBracesToken(c);
        }

        if(c == '[' || c == ']') {
            return createSquareBracesToken(c);
        }

        if(c == ':') {
            return createColonToken(c);
        }

        if(c == ',') {
            return createCommaToken(c);
        }

        if(c == '"') {
            return createStringToken();
        }

        if(c == 't') {
            return createTrueToken();
        }

        if(c == 'f') {
            return createFalseToken();
        }

        if(c == 'n') {
            return createNullToken();
        }

        if(c == '-' || Character.isDigit(c)) {
            return createNumberToken();
        }

        throw new Error(MessageFormat.format("Unexpected token at position {0} with char {1}", idx, c));
    }

    private JsonToken createFalseToken() {
        checkSubstringAndUpdateIndex("false");
        return BooleanToken.INSTANCE_FALSE;
    }

    private JsonToken createTrueToken() {
        checkSubstringAndUpdateIndex("true");
        return BooleanToken.INSTANCE_TRUE;
    }

    private JsonToken createNullToken() {
        checkSubstringAndUpdateIndex("null");
        return NullToken.INSTANCE;
    }

    private JsonToken createNumberToken() {
        return new NumberToken(getNumber());
    }

    private JsonToken createCommaToken(char c) {
        return CommaToken.INSTANCE;
    }
    private JsonToken createColonToken(char c) {
        return ColonToken.INSTANCE;
    }
    public JsonToken createCurlyBracesToken(char c) {
        return c == '{' ? CurlyBracketOpeningToken.INSTANCE : CurlyBracketClosingToken.INSTANCE;
    }

    private JsonToken createSquareBracesToken(char c) {
        return c == '[' ? SquareBracketOpeningToken.INSTANCE : SquareBracketClosingToken.INSTANCE;
    }

    private JsonToken createStringToken() {
        return new StringToken(getString());
    }

    private String getString() {
        int endIdx = idx;
        while(endIdx < jsonString.length()) {
            final char c = jsonString.charAt(endIdx++);
            if(c == '"') break;
            if(c == '\\') ++endIdx;
        }
        final String str = getSubstringAndUpdateIdx(endIdx - 1);
        ++idx;
        return str;
    }

    private String getNumber() {
        int endIdx = idx--;
        while(endIdx < jsonString.length() && Character.isDigit(jsonString.charAt(endIdx)))
            ++endIdx;
        if(endIdx < jsonString.length() && jsonString.charAt(endIdx) == '.') {
            ++endIdx;
            while(endIdx < jsonString.length() && Character.isDigit(jsonString.charAt(endIdx)))
                ++endIdx;
        }
        return getSubstringAndUpdateIdx(endIdx);
    }

    private void checkSubstringAndUpdateIndex(String str) {
        --idx;
        if(jsonString.substring(idx, idx+str.length()).equals(str)) {
            getSubstringAndUpdateIdx(idx+str.length());
            return;
        }
        throw new Error(MessageFormat.format("Expected \'{0}\'", str));
    }

    private String getSubstringAndUpdateIdx(int endIdx) {
        String s = jsonString.substring(idx, endIdx);
        idx = endIdx;
        skipSpaces();
        return s;
    }
}