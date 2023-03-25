package pt.isel.mpd.util;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

public class HttpRequest implements IRequest {

    private final AsyncHttpClient asyncHttpClient = Dsl.asyncHttpClient();

    @Override
    public CompletableFuture<Stream<String>> getContent(String path) {
        return asyncHttpClient
                .prepareGet(path)
                .execute()
                .toCompletableFuture()
                .thenApply(Response::getResponseBodyAsStream)
                .thenApply(in -> stream(new IteratorFromReader(in), false));
    }

    @Override
    public void close() throws Exception {
        if(!asyncHttpClient.isClosed())
            try {
                asyncHttpClient.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

}
