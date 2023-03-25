package pt.isel.mpd.util.requests;

import java.io.Reader;
import java.util.concurrent.CompletableFuture;

public interface AsyncRequest extends AutoCloseable {
    CompletableFuture<Reader> getBody(String path);
}