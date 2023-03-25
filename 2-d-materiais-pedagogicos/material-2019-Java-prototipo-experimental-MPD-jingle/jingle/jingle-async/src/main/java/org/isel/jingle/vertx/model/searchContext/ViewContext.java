package org.isel.jingle.vertx.model.searchContext;

/**
 * Auxiliary container that holds all necessary model data for a text view
 * and extra context
 */
public class ViewContext<T, R> {
    private final T model;
    private final R context;

    public ViewContext(T model, R context) {
        this.model = model;
        this.context = context;
    }

    public T getModel() {
        return model;
    }

    public R getContext() {
        return context;
    }
}
