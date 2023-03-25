package util;

import org.mockito.internal.util.io.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;

/**
 * Class used for test purposes to avoid do a lot of http requests.
 */
public class FileRequest implements IRequest{
    @Override
    public CompletableFuture<String> getBody(String uri) {
        String path = UriToFilenameParser.parse( uri );

        try{
            System.out.println(path);
            InputStream inputStream = ClassLoader.getSystemResource(path).openStream();
            String ret = IOUtil.readLines(inputStream).stream().reduce(String::concat).get();
            return CompletableFuture.completedFuture(ret);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
