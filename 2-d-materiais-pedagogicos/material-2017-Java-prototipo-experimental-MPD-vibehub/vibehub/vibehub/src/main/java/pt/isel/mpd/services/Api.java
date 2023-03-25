package pt.isel.mpd.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pt.isel.mpd.util.IRequest;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static pt.isel.mpd.util.StringUtils.join;

public class Api {

    private final IRequest req;

    public Api(IRequest req){
        this.req = req;
    }

    protected <R> CompletableFuture<R> get(String host, String sufix, String key, String field, Function<JsonObject,R> f, Object... params){
        String path = host + sufix + key;
        String url = String.format(path, params);
        return req.getContent(url)
                .thenApply(str -> {
                    JsonElement e = new JsonParser().parse(join(str));
                    JsonObject obj = e.getAsJsonObject().getAsJsonObject(field);
                    return f.apply(obj);
                });
    }
}
