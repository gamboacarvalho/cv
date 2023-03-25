package org.isel.jingle.vertx.view.artists;

import com.github.jknack.handlebars.Template;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.vertx.core.http.HttpServerResponse;
import org.isel.jingle.model.Album;
import org.isel.jingle.vertx.model.searchContext.ArtistAlbumSearchContext;
import org.isel.jingle.vertx.model.searchContext.ViewContext;
import org.isel.jingle.vertx.printStream.ResponsePrintStream;
import org.isel.jingle.vertx.view.BaseTextView;

import java.io.IOException;

public class ArtistAlbumsTextView extends BaseTextView<Observable<Album>, ArtistAlbumSearchContext> {
    private final Template header;
    private final Template body;
    private final Template emptySearch;

    public ArtistAlbumsTextView(ResponsePrintStream printStream, Template titleTemplate, Template header, Template body, Template emptySearch) {
        super(printStream, titleTemplate, true);
        this.header = header;
        this.body = body;
        this.emptySearch = emptySearch;
    }

    @Override
    protected void writeResponseBody(HttpServerResponse resp, ViewContext<Observable<Album>, ArtistAlbumSearchContext> viewContext) {
        viewContext.getModel().subscribeWith(new Observer<>() {
            int count = 0;

            @Override
            public void onSubscribe(Disposable d) {
                startBody(resp);
                try {
                    printStream.write(resp, header.apply(viewContext.getContext()));
                } catch (IOException e) {
                    handleException(resp, "ArtistAlbumsTextView - OnSubscribe", e);
                }
            }

            @Override
            public void onNext(Album album) {
                try {
                    count++;
                    printStream.write(resp, body.apply(album));

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
