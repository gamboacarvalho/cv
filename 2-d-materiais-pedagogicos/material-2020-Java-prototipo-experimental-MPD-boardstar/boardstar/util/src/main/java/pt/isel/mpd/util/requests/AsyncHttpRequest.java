package pt.isel.mpd.util.requests;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class AsyncHttpRequest implements AsyncRequest {

    private final static Logger LOGGER = Logger.getLogger(AsyncHttpRequest.class.getName());
    AsyncHttpClient ahc = Dsl.asyncHttpClient();

    @Override
    public CompletableFuture<Reader> getBody(String path) {
        return ahc
                .prepareGet(path)
                .execute()
                .toCompletableFuture()
                .thenApply(Response::getResponseBodyAsStream)
                .thenApply(InputStreamReader::new);
    }

    @Override
    public void close() throws Exception {
        if(ahc!=null)
            ahc.close();
    }
}