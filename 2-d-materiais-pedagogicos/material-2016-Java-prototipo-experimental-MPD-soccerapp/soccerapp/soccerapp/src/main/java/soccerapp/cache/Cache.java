package soccerapp.cache;

import util.DiskOperations;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Cache {

    protected final LRUCache<String, CompletableFuture<String>> LRU;

    protected Cache(int capacity) {
        LRU = new LRUCache<>(capacity);
    }

    protected Optional<String> getFromMem(String path){
        CompletableFuture<String> promise = LRU.get(path);
        if(promise != null)
            return Optional.of(promise.join());
        return Optional.empty();
    }

    protected Optional<String> getFromDisk(String path){
        try {
            File file = DiskOperations.getFile(path);
            if (file.exists())
                return DiskOperations.readFile(file.getAbsolutePath());
            return Optional.empty();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getOrDefault(String path, Supplier<CompletableFuture<String>> supplier){
        Optional<String> representation = getFromMem(path);
        String result;
        if(representation.isPresent())
            //already got representation in memory
            return representation.get();

        //let's promise we will get the representation
        //let's check the disk
        representation = getFromDisk(path);

        if(representation.isPresent()){
            //let's fulfill the promise
            result = representation.get();
            LRU.put(path, CompletableFuture.completedFuture(result));
            return result;
        }

        CompletableFuture<String> orDefault = LRU.getOrDefault(path, supplier);
        //not in memory nor in disk, let's get it from the source
        result = orDefault.join();
        //ok, got it from web, let's add to disk (the promise is already in memory)
        DiskOperations.writeToFileAsync(path,result);

        return result;
    }
}
