package org.isel.jingle.vertx.view.artists;

import com.github.jknack.handlebars.Template;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.vertx.core.http.HttpServerResponse;
import org.isel.jingle.model.Track;
import org.isel.jingle.vertx.model.searchContext.ArtistTracksSearchContext;
import org.isel.jingle.vertx.model.searchContext.ViewContext;
import org.isel.jingle.vertx.printStream.ResponsePrintStream;
import org.isel.jingle.vertx.view.BaseTextView;

import java.io.IOException;

public class ArtistTracksTextView extends BaseTextView<Observable<Track>, ArtistTracksSearchContext> {
    private final Template header;
    private final Template body;
    private final Template emptySearch;

    public ArtistTracksTextView(ResponsePrintStream printStream, Template titleTemplate, Template header, Template body, Template emptySearch) {
        super(printStream, titleTemplate, true);
        this.header = header;
        this.body = body;
        this.emptySearch = emptySearch;
    }


    @Override
    protected void writeResponseBody(HttpServerResponse resp, ViewContext<Observable<Track>, ArtistTracksSearchContext> viewContext) {
        viewContext.getModel().subscribeWith(new Observer<>() {
            int count = 0;

            @Override
            public void onSubscribe(Disposable d) {
                startBody(resp);
                try {
                    printStream.write(resp, header.apply(viewContext.getContext()));
                } catch (IOException e) {
                    handleException(resp, "ArtistTracksTextView - OnSubscribe", e);
                }
            }

            @Override
            public void onNext(Track track) {
                try {
                    count++;
                    printStream.write(resp, body.apply(track));

                } catch (IOException e) {
                    handleException(resp, "ArtistAlbumsTextView - OnNext", e);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (count == 0) {
                    printStream.write(resp, emptySearch.text());
                }

                handleException(resp, "ArtistAlbumsTextView - OnError", e);
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
