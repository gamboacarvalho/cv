package org.isel.jingle.vertx.view.albums;

import com.github.jknack.handlebars.Template;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.vertx.core.http.HttpServerResponse;
import org.isel.jingle.model.Track;
import org.isel.jingle.vertx.model.searchContext.AlbumTracksSearchContext;
import org.isel.jingle.vertx.model.searchContext.ViewContext;
import org.isel.jingle.vertx.printStream.ResponsePrintStream;
import org.isel.jingle.vertx.view.BaseTextView;

import java.io.IOException;

public class AlbumTracksTextView extends BaseTextView<Observable<Track>, AlbumTracksSearchContext> {
    private final Template header;
    private final Template body;
    private final Template emptySearch;


    public AlbumTracksTextView(ResponsePrintStream printStream, Template titleTemplate, Template header, Template body, Template emptySearch) {
        super(printStream, titleTemplate, true);
        this.header = header;
        this.body = body;
        this.emptySearch = emptySearch;
    }

    @Override
    protected void writeResponseBody(HttpServerResponse resp, ViewContext<Observable<Track>, AlbumTracksSearchContext> model) {
        model.getModel().subscribeWith(new Observer<>() {
            int count = 0;

            @Override
            public void onSubscribe(Disposable d) {
                startBody(resp);
                try {
                    printStream.write(resp, header.apply(model.getContext()));
                } catch (IOException e) {
                    handleException(resp, "AlbumTracksView - OnSubscribe", e);
                }
            }

            @Override
            public void onNext(Track albumTracks) {
                try {
                    count++;
                    printStream.write(resp, body.apply(albumTracks));

                } catch (IOException e) {
                    if (count == 0) {
                        printStream.write(resp, emptySearch.text());
                    }

                    handleException(resp, "AlbumTracksView - OnNext", e);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (count == 0) {
                    printStream.write(resp, emptySearch.text());
                }

                handleException(resp, "AlbumTracksView - onError", e);
            }

            @Override
            public void onComplete() {
                closeBody(resp);
                closeResponse(resp);
            }
        });
    }
}
