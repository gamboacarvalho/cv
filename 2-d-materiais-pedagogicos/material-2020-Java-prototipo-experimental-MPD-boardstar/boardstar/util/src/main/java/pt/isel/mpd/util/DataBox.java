package pt.isel.mpd.util;

import java.time.LocalDateTime;

public class DataBox<T> {

    private final T content;
    private final LocalDateTime timestamp;
    private final long expirationTime;

    /**
     * @param content - Value to be saved
     * @param expirationTime - Expiration time in hours
     * */
    public DataBox(T content, long expirationTime){
        this.content = content;
        this.expirationTime = expirationTime;
        timestamp = LocalDateTime.now();
    }

    public boolean expired() {
        return LocalDateTime.now().isAfter(timestamp.plusSeconds(expirationTime));
    }

    public T getContent() {
        return content;
    }
}