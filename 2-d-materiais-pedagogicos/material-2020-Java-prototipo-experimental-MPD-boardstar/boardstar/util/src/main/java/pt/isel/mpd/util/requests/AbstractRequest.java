package pt.isel.mpd.util.requests;

import pt.isel.mpd.util.Request;
import pt.isel.mpd.util.iterators.IteratorInputStream;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public abstract class AbstractRequest implements Request {

    @Override
    final public Iterable<String> getLines(String path) {
        return () -> new IteratorInputStream(getStream(path));
    }

    @Override
    final public Reader getReader(String path) {
        return new InputStreamReader(getStream(path));
    }

    public abstract InputStream getStream(String path);
}
