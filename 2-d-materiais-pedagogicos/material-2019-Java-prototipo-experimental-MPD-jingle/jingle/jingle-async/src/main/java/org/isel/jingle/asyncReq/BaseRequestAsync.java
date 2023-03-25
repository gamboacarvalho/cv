package org.isel.jingle.asyncReq;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class BaseRequestAsync implements AsyncRequest {
    private static final Duration MAX_TIMEOUT = Duration.ofMinutes(1);

    //Creates a new httpClient with default settings (GET, Http/2, never redirect)
    private HttpClient httpClient = HttpClient.newHttpClient();

    @Override
    public CompletableFuture<String> getBody(String path) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .timeout(MAX_TIMEOUT)
                .build();

        return httpClient
                .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}
