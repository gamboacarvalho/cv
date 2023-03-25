package org.isel.jingle.vertx.view;

import com.github.jknack.handlebars.Template;
import io.vertx.core.http.HttpServerResponse;
import org.isel.jingle.vertx.model.searchContext.ViewContext;
import org.isel.jingle.vertx.printStream.ResponsePrintStream;

public abstract class BaseTextView<T, R> implements View<T, R> {
    protected final ResponsePrintStream printStream;
    private final Template titleTemplate;
    private final boolean modelNotNull;

    public BaseTextView(ResponsePrintStream printStream, Template titleTemplate, boolean modelNotNull) {
        this.printStream = printStream;
        this.titleTemplate = titleTemplate;
        this.modelNotNull = modelNotNull;
    }


    @Override
    public void write(HttpServerResponse resp) {
        if (modelNotNull) {
            throw new UnsupportedOperationException("This view requires a Model. You should invoke write(resp, model) instead!");
        }
        write(resp, null);
    }


    @Override
    public final void write(HttpServerResponse resp, ViewContext<T, R> viewContext) {
        resp.setChunked(true);
        resp.putHeader("content-type", "text/html");

        printStream.write(resp, "<html>");
        printStream.write(resp, titleTemplate.text());
        writeResponseBody(resp, viewContext);

        //
        //Cannot close response right now as 'writeResponseBody' method is async
        //and returns immediately
        //
        //Instead, the response should be closed on the method 'onComplete' (where applicable)
        //
    }

    protected abstract void writeResponseBody(HttpServerResponse resp, ViewContext<T, R> model);


    protected void handleException(HttpServerResponse resp, String level, Throwable throwable) {
        resp.setStatusCode(500);
        closeBody(resp);
        closeResponse(resp);

        System.out.println("---Produced an error on class " + level + " with following message:");
        System.out.println(throwable.getMessage());
    }

    protected void startBody(HttpServerResponse resp) {
        printStream.write(resp, "<body>");
    }

    protected void closeBody(HttpServerResponse resp) {
        printStream.write(resp, "</body>");
    }

    protected void closeResponse(HttpServerResponse resp) {
        printStream.write(resp, "</html>");
        resp.end();
    }
}
