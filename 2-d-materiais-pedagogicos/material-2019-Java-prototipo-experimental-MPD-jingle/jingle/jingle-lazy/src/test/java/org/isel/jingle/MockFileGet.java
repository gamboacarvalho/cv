package org.isel.jingle;

import org.isel.jingle.util.req.MockRequest;

import java.io.InputStream;
import java.util.function.Function;

public class MockFileGet implements Function<String, InputStream> {
    private static int count = 0;

    @Override
    public InputStream apply(String path) {
        System.out.println("Requesting..." + ++count + " from " + path);
        return MockRequest.openStream(path);
    }
}
