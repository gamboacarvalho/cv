package pt.isel.mpd.util;

import java.util.function.Function;
import java.util.function.Supplier;

public class RequestObject<T,R> {
    private final Supplier<T> supplier;
    private final Function<T,R> getter;
    private int page = 0;
    private R value;

    public RequestObject(Supplier<T> supplier, Function<T, R> getter) {
        this.supplier = supplier;
        this.getter = getter;
    }


    public RequestObject<T,R> incrPage() {
        this.page++;
        return this;
    }


    public int getPage() {
        return page;
    }

    public R getValue() {
        if (value == null) {
            value = getter.apply(supplier.get());
        }
        return value;
    }
}