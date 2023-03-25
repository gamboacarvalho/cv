package soccerapp.cache.domain;

import soccerapp.cache.Cache;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class TeamCache extends Cache {

    public TeamCache(int capacity) {
        super(capacity);
    }

}
