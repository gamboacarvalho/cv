package pt.isel.mpd.util.requests;

import java.io.InputStream;

public class MockRequest extends AbstractRequest {
    @Override
    public InputStream getStream(String path) {
        int code = path.substring(0, path.length() - 10).hashCode();
        return ClassLoader.getSystemClassLoader().getResourceAsStream(code + ".json");
    }
}