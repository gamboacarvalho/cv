package util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Class used for test purposes to avoid do a lot of http requests.
 */
public class FileRequest implements IRequest{
    @Override
    public InputStream getBody(String uri) {
        String path = UriToFilenameParser.parse( uri );

        try{
            System.out.println(path);
            return ClassLoader.getSystemResource(path).openStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
