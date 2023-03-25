package org.isel.jingle.vertx.view;

import io.vertx.core.http.HttpServerResponse;
import org.isel.jingle.vertx.model.searchContext.ViewContext;

public interface View<T, R> {
    void write(HttpServerResponse resp);

    void write(HttpServerResponse resp, ViewContext<T, R> model);
}
