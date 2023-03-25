package org.isel.jingle.api;

import org.isel.jingle.LastFmApi;
import org.isel.jingle.LastFmFileApi;
import org.isel.jingle.util.req.BaseRequest;
import org.isel.jingle.util.req.MockRequest;

public class LastFmlFileAPiTest extends LastFmApiTests {

    @Override
    protected LastFmApi getLastFmApi() {
        BaseRequest request = new BaseRequest(MockRequest::openStream);
        return new LastFmFileApi(request);
    }
}
