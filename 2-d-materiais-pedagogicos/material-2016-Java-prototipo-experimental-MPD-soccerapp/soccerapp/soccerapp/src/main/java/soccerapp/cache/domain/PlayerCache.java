package soccerapp.cache.domain;

import soccerapp.cache.Cache;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class PlayerCache extends Cache {

    public PlayerCache(int capacity) {
        super(capacity);
    }

}
