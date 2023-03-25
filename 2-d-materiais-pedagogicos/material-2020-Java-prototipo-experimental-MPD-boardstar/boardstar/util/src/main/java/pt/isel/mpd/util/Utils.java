package pt.isel.mpd.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Function;

public class Utils {

    public static <T> String extractStringValue(Optional<T> optional, Function<T, String> getter, String default_) {
        return optional.isPresent()? getter.apply(optional.get()) : default_;
    }

    // Decodes url parameter
    public static String decodeParam(String param) {
        return param == null? null : URLDecoder.decode(param, StandardCharsets.UTF_8);
    }

    // Encodes url parameter
    public static String encodeParam(String param) {
        return URLEncoder.encode(param, StandardCharsets.UTF_8);
    }

    public static String encodeUTF8(String str) { return new String(str.getBytes(), StandardCharsets.UTF_8);}
}
