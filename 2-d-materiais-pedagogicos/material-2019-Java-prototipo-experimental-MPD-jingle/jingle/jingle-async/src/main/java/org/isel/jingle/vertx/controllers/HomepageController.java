package org.isel.jingle.vertx.controllers;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.isel.jingle.JingleService;
import org.isel.jingle.vertx.view.homepage.HomepageTextView;

public class HomepageController extends BaseController {
    private final HomepageTextView textView;

    public HomepageController(Router router, JingleService service, HomepageTextView textView) {
        super(router, service);
        this.textView = textView;
    }

    @Override
    protected void addRoutes(Router router) {
        router.route("/").handler(this::sendHomepage);
    }

    private void sendHomepage(RoutingContext ctx) {
        textView.write(ctx.response());
    }
}
