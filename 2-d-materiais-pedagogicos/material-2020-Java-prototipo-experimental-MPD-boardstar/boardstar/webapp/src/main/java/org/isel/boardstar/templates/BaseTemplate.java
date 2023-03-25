package org.isel.boardstar.templates;

import java.nio.charset.StandardCharsets;

import static pt.isel.mpd.util.Utils.encodeParam;
import static pt.isel.mpd.util.Utils.encodeUTF8;

public abstract class BaseTemplate {

    private final String name;
    private final String id;

    BaseTemplate(String name, String id) {
        this.name = encodeUTF8(name);
        this.id = encodeParam(encodeUTF8(id));
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
