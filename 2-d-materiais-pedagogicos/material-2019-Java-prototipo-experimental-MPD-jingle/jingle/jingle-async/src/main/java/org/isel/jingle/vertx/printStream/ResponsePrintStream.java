package org.isel.jingle.vertx.printStream;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;

public class ResponsePrintStream {

    public void write(HttpServerResponse response, String str) {
        Buffer buffer = Buffer.buffer(str);
        response.write(buffer);
    }
}

