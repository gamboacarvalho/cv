/*
 * Copyright (c) 2017, Miguel Gamboa
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

package util;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 * @author Miguel Gamboa
 *         created on 16-02-2017
 */
public class HttpRequest implements IRequest {

    @Override
    public CompletableFuture<String> getBody(String path) {
        AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();
        return asyncHttpClient
                .prepareGet(path)
                .execute()
                .toCompletableFuture()
                .thenApply(Response::getResponseBody)
                .whenComplete((res,ex) -> closeAHC(asyncHttpClient));
    }

    static void closeAHC(AsyncHttpClient client) {
        try{
            client.close();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}