
package pt.isel.mpd.util;

import java.io.*;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;

public class IteratorFromReader extends AbstractSpliterator<String> {

    private BufferedReader reader;

    public IteratorFromReader(InputStream in) {
        super(Long.MAX_VALUE, ORDERED);
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public boolean tryAdvance(Consumer<? super String> action) {
        try {
            if(reader == null)
                return false;
            String line = reader.readLine();
            if(line == null) {
                reader.close();
                reader = null;
                return false;
            }
            action.accept(line);
            return true;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
