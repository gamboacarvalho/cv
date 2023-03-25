package org.isel.jingle.vertx.view.artists;

import com.github.jknack.handlebars.Template;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.vertx.core.http.HttpServerResponse;
import org.isel.jingle.model.Artist;
import org.isel.jingle.vertx.model.searchContext.ArtistSearchContext;
import org.isel.jingle.vertx.model.searchContext.ViewContext;
import org.isel.jingle.vertx.printStream.ResponsePrintStream;
import org.isel.jingle.vertx.view.BaseTextView;

import java.io.IOException;

public class ArtistTextView extends BaseTextView<Observable<Artist>, ArtistSearchContext> {
    private final Template header;
    private final Template body;
    private final Template emptySearch;


    public ArtistTextView(ResponsePrintStream printStream, Template title, Template header, Template body, Template emptySearch) {
        super(printStream, title, true);
        this.header = header;
        this.body = body;
        this.emptySearch = emptySearch;
    }

    @Override
    protected void writeResponseBody(HttpServerResponse resp, ViewContext<Observable<Artist>, ArtistSearchContext> viewContext) {
        viewContext.getModel().subscribeWith(new Observer<>() {
            int count = 0;

            @Override
            public void onSubscribe(Disposable d) {
                startBody(resp);

                try {
                    printStream.write(resp, header.apply(viewContext.getContext()));

                } catch (IOException e) {
                    handleException(resp, "ArtistTextView - OnSubscribe", e);
                }
            }

            @Override
            public void onNext(Artist artist) {
                try {
                    count++;
                    printStream.write(resp, body.apply(artist));

                } catch (IOException e) {
                    handleException(resp, "ArtistTextView - OnNext", e);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (count == 0) {
                    printStream.write(resp, emptySearch.text());
                }

                handleException(resp, "ArtistTextView - onError", e);
            }

            @Override
            public void onComplete() {
                if (count == 0) {
                    printStream.write(resp, emptySearch.text());
                }

                closeBody(resp);
                closeResponse(resp);
            }
        });
    }

}
