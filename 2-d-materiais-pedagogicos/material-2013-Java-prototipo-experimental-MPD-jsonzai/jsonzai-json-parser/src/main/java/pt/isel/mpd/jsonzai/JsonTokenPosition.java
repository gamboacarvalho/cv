package pt.isel.mpd.jsonzai;


import java.util.function.BiFunction;

public class JsonTokenPosition {
    public final int idx;

    /**
     * We could use a Supplier that captured the idx and src context (the dynamic
     * arguments) at creation time.
     * However that solution would promote several resolutions of the invokedynamic to
     * different call sites that capture different values of dynamic arguments.
     * So, to reuse the same lambda object, returned by the resolution of the call sites,
     * we prefered to use a Function althought in some cases we are ignoring the arguments.
     */
    public final BiFunction<Integer, String, JsonToken> tokenProvider;

    public JsonTokenPosition(int idx, BiFunction<Integer, String, JsonToken> tokenizer) {
        this.idx = idx;
        this.tokenProvider = tokenizer;
    }
}
