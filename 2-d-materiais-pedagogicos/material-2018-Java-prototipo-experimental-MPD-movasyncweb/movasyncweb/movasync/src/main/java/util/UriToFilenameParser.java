package util;

public class UriToFilenameParser {
    public static String parse(String uri ){
        String[] parts = uri.split("/");

        return parts[parts.length-1]
                    .replace('?', '-')
                    .replace('&', '-')
                    .replace('=', '-')
                    .replace(',', '-')
                    .substring(0, parts[parts.length-1].length());
    }
}
