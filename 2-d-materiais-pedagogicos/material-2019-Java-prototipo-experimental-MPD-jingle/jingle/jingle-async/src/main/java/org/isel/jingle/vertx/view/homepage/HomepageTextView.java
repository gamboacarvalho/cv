package org.isel.jingle.vertx.view.homepage;

import com.github.jknack.handlebars.Template;
import io.vertx.core.http.HttpServerResponse;
import org.isel.jingle.vertx.model.searchContext.ViewContext;
import org.isel.jingle.vertx.printStream.ResponsePrintStream;
import org.isel.jingle.vertx.view.BaseTextView;

public class HomepageTextView extends BaseTextView {
    private final Template pageTemplate;


    public HomepageTextView(ResponsePrintStream printStream, Template titleTemplate, Template pageTemplate) {
        super(printStream, titleTemplate, false);
        this.pageTemplate = pageTemplate;
    }

    @Override
    protected void writeResponseBody(HttpServerResponse resp, ViewContext model) {
        printStream.write(resp, pageTemplate.text());
        closeResponse(resp);
    }
}
