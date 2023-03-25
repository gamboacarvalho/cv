package pt.isel.mpd.util.requests;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.CompletableFuture;

public class AsyncMockRequest implements AsyncRequest {

    @Override
    public CompletableFuture<Reader> getBody(String path) {
        return CompletableFuture.completedFuture(path.substring(0, path.length() - 10).hashCode())
                .thenApply(code -> ClassLoader.getSystemClassLoader().getResourceAsStream(code + ".json"))
                .thenApply(InputStreamReader::new);
    }

    @Override
    public void close() {

    }
}