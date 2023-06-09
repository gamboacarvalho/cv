package pt.isel.mpd.util.iterators;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorInputStream implements Iterator<String> {
    BufferedReader reader;
    String line;
    boolean called;

    public IteratorInputStream(InputStream in) {
        reader = new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public boolean hasNext() {
        if(called) return line != null;
        line = nextLine();
        called = true;
        if(line == null) closeReader();
        return line != null;
    }

    private void closeReader() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String nextLine() {
        try {
            return reader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String next() {
        if(!hasNext()) throw new NoSuchElementException();
        called = false;
        return line;
    }
}
