package org.isel.jingle.asyncReq;


import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface AsyncRequest {
    CompletableFuture<String> getBody(String path);
}
