package pt.isel.mpd.util;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class StringUtils {

    public static <T> String join(Stream<T> src){
        return src.map(Object::toString).collect(joining());
        //.reduce("", (s,e) -> s.concat(e.toString()), String::concat);
    }
}
