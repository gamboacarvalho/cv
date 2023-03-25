package soccerapp.cache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.function.Supplier;

public class LRUCache<K, V> {

    private final int capacity;
    private LinkedHashMap<K, V> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<>(capacity);
    }

    public V getOrDefault(K key, Supplier<V> promiseSupplier){
        V value = cache.get(key);
        if (value == null)
            value = promiseSupplier.get();
        this.put(key, value);

        return value;
    }

    public V get(K key){
        return cache.get(key);
    }

    public void put(K key, V value){
        if (cache.containsKey(key))
            cache.remove(key);
        else if(cache.size() == capacity){
            Iterator<K> it = cache.keySet().iterator();
            it.next();
            it.remove();
        }
        cache.put(key, value);
    }
}
