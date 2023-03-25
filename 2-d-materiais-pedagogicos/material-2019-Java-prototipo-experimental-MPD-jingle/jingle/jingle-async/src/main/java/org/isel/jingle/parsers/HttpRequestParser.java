package org.isel.jingle.parsers;

import java.util.Objects;

public class HttpRequestParser {
    //TODO - Replace name for web parser or similar
    public String removeWhitespaces(String input) {
        Objects.requireNonNull(input);
        return input.replaceAll("\\s", "%20");
    }
}
