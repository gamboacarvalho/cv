package pt.isel.mpd.util.requests;

import java.io.Reader;
import java.util.concurrent.CompletableFuture;

public class AsyncRequestMediator implements AsyncRequest {
    private AsyncRequest req;
    int count;
    public AsyncRequestMediator(AsyncRequest req ) {
        this.req = req;
    }
    public CompletableFuture<Reader> getBody(String path) {
        ++count;
        return req.getBody(path);
    }

    public int getCount() {
        return count;
    }

    @Override
    public void close() throws Exception {
        req.close();
    }
}
