package pt.isel.mpd.jsonzai;

import com.google.common.collect.ImmutableMap;
import pt.isel.mpd.jsonzai.JsonToken.BooleanToken;
import pt.isel.mpd.jsonzai.JsonToken.NumberToken;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

import static pt.isel.mpd.jsonzai.JsonToken.*;

/**
 * Instances of this class parse Json strings and returned filled Java objects and/or values
 */
public class JsonParser {
    JsonTokenProvider tokenProvider;


    public interface TriFunction<T, U, V, R> {
        R apply(T t, U u, V v) throws Exception;
    }

    private static Map<Class<?>, List<Field>> typeFields = new HashMap<>();

    private static final Map<Class<?>, Function<String, Object>> primitiveNumbersConverters =
            new ImmutableMap.Builder<Class<?>, Function<String, Object>>()
                    .put(Integer.TYPE, Integer::parseInt)
                    .put(Integer.class, Integer::parseInt)
                    .put(Long.TYPE, Long::parseLong)
                    .put(Long.class, Long::parseLong)
                    .put(Float.TYPE, Float::parseFloat)
                    .put(Double.TYPE, Double::parseDouble)
                    .put(String.class, Object::toString)
                    .put(Object.class, Object::toString)
                    .put(Date.class, JsonParser::parseDate)
                    .build();

    private final Map<JsonToken, TriFunction<JsonToken, Class<?>, Field, Object>> parserStrategies =
            new ImmutableMap.Builder<JsonToken, TriFunction<JsonToken, Class<?>, Field, Object>>()
                    .put(BooleanToken.INSTANCE_TRUE, this::parseBoolean)
                    .put(BooleanToken.INSTANCE_FALSE, this::parseBoolean)
                    .put(NumberToken.INSTANCE, this::parseStringToObject)
                    .put(StringToken.INSTANCE, this::parseStringToObject)
                    .put(NullToken.INSTANCE, this::parseNull)
                    .put(CurlyBracketOpeningToken.INSTANCE, this::parseObject)
                    .put(SquareBracketOpeningToken.INSTANCE, this::parseArray)
                    .build();

    public JsonParser(JsonTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public JsonParser() {
        this.tokenProvider = new JsonTokenProvider();
    }

    public <T> List<T> toList(String jsonStr, Class<T> dest){
        try {
            tokenProvider.setString(jsonStr);
            JsonToken token = tokenProvider.getNextToken();
            return (List<T>)internalToObject(token, dest, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T toObject(String jsonStr, Class<T> t) {
        try {
            tokenProvider.setString(jsonStr);
            JsonToken token = tokenProvider.getNextToken();
            return (T)internalToObject(token, t, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object internalToObject(JsonToken token, Class<?> t, Field f) throws Exception {
        return parserStrategies.get(token).apply(token, t, f);
    }

    private Object parseObject(JsonToken token, Class<?> t, Field f) throws Exception{
        Object o = t.newInstance();
        while(true) {
            parseJsonNameValue(t, o);
            JsonToken nextToken = tokenProvider.getNextToken();
            if (nextToken == CurlyBracketClosingToken.INSTANCE) {
                return o;
            }

            if (!(nextToken == CommaToken.INSTANCE)) {
                throw new Exception("Expected ',' or '}'");
            }
        }
    }

    private void parseJsonNameValue(Class<?> t, Object o) throws Exception {
        JsonToken nameToken = tokenProvider.getNextToken();
        JsonToken colonToken = tokenProvider.getNextToken();
        if (!(colonToken == ColonToken.INSTANCE)) {
            throw new Exception("Expected ':' and got " + colonToken.getValue());
        }

        Optional<Field> f = getField(t, nameToken.getValue());
        Class<?> fieldType = f.isPresent() ? f.get().getType() : Object.class;
        Object value = internalToObject(tokenProvider.getNextToken(), fieldType, f.orElse(null));
        if(f.isPresent()) {
            f.get().setAccessible(true);
            f.get().set(o, value);
        }
    }

    private Object parseArray(JsonToken token, Class<?> t, Field f) throws Exception {
        List a = new ArrayList();
        Class<?> memberType = t;
        if(f != null)
                memberType = (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0];
        token = tokenProvider.getNextToken();
        while(token != SquareBracketClosingToken.INSTANCE) {
            Object o = internalToObject(token, memberType, null);
            a.add(o);

            token = tokenProvider.getNextToken();
            if(token == SquareBracketClosingToken.INSTANCE) {
                break;
            }
            if(token != CommaToken.INSTANCE) {
                throw new Exception("Expected ',' or ']'");
            }
            token = tokenProvider.getNextToken();
        }
        return a;
    }

    private static Date parseDate(String src){
        final SimpleDateFormat DATE_FORMATTER  = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return DATE_FORMATTER.parse(src);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Object parseBoolean(JsonToken token, Class<?> t, Field f) {
        return Boolean.parseBoolean(token.getValue());
    }

    private Object parseNull(JsonToken token, Class<?> t, Field f) {
        return null;
    }

    private Object parseStringToObject(JsonToken token, Class<?> fieldClass, Field f) {
        return primitiveNumbersConverters.get(fieldClass).apply(token.getValue());
    }


    private  Optional<Field> getField(Class<?> t, String fieldName) {
        List<Field> fields = typeFields.get(t);
        if(fields == null) {
            typeFields.put(t, fields = getFieldsForType(t));
        }
        return fields.stream().filter(f -> f.getName().equalsIgnoreCase(fieldName)).findFirst();
    }

    private List<Field> getFieldsForType(Class<?> t) {
        ArrayList<Field> fields = new ArrayList<>();
        while(t != Object.class) {
            fields.addAll(Arrays.asList(t.getDeclaredFields()));
            t = t.getSuperclass();
        }
        return fields;
    }



}