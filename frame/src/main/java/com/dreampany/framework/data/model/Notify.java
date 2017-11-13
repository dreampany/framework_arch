package com.dreampany.framework.data.model;

import com.dreampany.framework.data.enums.Type;

/**
 * Created by air on 10/23/17.
 */

public class Notify<T extends Type> extends BaseSerial {

    private String title;
    private String subtitle;
    private T type;

    public Notify(T type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setType(T type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public T getType() {
        return type;
    }
}
