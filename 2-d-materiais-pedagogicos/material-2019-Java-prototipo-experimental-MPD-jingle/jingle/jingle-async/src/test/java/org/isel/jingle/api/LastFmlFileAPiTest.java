package org.isel.jingle.api;

import org.isel.jingle.mock.MockAsyncRequest;

public class LastFmlFileAPiTest extends LastFmApiTests {
    @Override
    protected LastFmApi getLastFmApi() {
        MockAsyncRequest request = new MockAsyncRequest();
        return new LastFmWebApi(request);
    }
}
