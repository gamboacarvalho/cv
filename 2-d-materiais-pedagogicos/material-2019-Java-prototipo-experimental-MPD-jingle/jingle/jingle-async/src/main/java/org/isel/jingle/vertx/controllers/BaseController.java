package org.isel.jingle.vertx.controllers;

import io.vertx.ext.web.Router;
import org.isel.jingle.JingleService;

public abstract class BaseController<T> {
    final JingleService service;

    public BaseController(Router router, JingleService service) {
        this.service = service;
        addRoutes(router);
    }

    protected abstract void addRoutes(Router router);
}
