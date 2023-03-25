package org.isel.boardstar.templates;

import static pt.isel.mpd.util.Utils.encodeParam;

public class GameTemplate extends BaseTemplate{

    private final String description;
    private final String categories;

    public GameTemplate(String name, String id, String description, String categories) {
        super(name, id);
        this.description = description;
        this.categories = encodeParam(categories);
    }

    public String getDescription() {
        return description;
    }

    public String getCategories() {
        return categories;
    }
}
