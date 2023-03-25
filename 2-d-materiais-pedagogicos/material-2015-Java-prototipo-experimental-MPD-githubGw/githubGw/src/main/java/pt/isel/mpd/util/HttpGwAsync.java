/*
 * Copyright (C) 2015 Miguel Gamboa at CCISEL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.isel.mpd.util;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Created by Miguel Gamboa on 05-06-2015.
 */
public class HttpGwAsync implements AutoCloseable {

    private final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    private final AtomicInteger nrOfRequests = new AtomicInteger(0);
    private final AtomicInteger nrOfResponses = new AtomicInteger(0);

    public int getNrOfRequests() {
        return nrOfRequests.get();
    }

    public int getNrOfResponses() {
        return nrOfResponses.get();
    }

    public CompletableFuture<Response> getDataAsync(
            String path,
            Pair<String, String>...headers)
    {
        System.out.println( path);
        final CompletableFuture<Response> promise = new CompletableFuture<>();
        AsyncHttpClient.BoundRequestBuilder request = asyncHttpClient.prepareGet(path);
        for (Pair<String, String> p : headers) request.addHeader(p.key, p.value);
        request.execute(asyncHandler(promise));
        nrOfRequests.incrementAndGet();
        return promise;
    }

    private AsyncCompletionHandler<Response> asyncHandler(final CompletableFuture<Response> promise)
    {
        return new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                nrOfResponses.incrementAndGet();
                promise.complete(response);
                return response;
            }
        };
    }

    @Override
    public void close() throws Exception
    {
        if (!asyncHttpClient.isClosed())
            asyncHttpClient.close();
    }

    public boolean isClosed() {
        return asyncHttpClient.isClosed();
    }

}
