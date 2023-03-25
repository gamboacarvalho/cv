package org.isel.jingle.mock;

/**
 * Receives an URI and formats it to another that is compatible to save on the file System
 */
public class MockUriFormatter {
    public static String formatUri(String uri) {
        String[] parts = uri.split("/");

        return parts[parts.length - 1]
                .replace('?', '-')
                .replace('&', '-')
                .replace('=', '-')
                .replace(',', '-')
                .substring(0, 68);
    }

}
