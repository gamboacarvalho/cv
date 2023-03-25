package pt.isel.mpd.util.requests;

import java.io.InputStream;

public class RequestMediator extends AbstractRequest {
    private AbstractRequest req;
    int count;
    public RequestMediator(AbstractRequest req ) {
        this.req = req;
    }
    public InputStream getStream(String path) {
        ++count;
        return req.getStream(path);
    }

    public int getCount() {
        return count;
    }
}
