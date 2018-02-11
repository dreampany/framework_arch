package com.dreampany.framework.data.api.swipe;

/**
 * Created by air on 11/3/17.
 */

public class SwipeItem {
    static final String UNSELECTED_ITEM_VALUE = "com.dreampany.swipe.UNSELECTED_ITEM_VALUE";

    private String value;
    private String title;
    private String description;

    public SwipeItem() {
    }

    public SwipeItem(String value, String title, String description) {
        this.value = value;
        this.title = title;
        this.description = description;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    boolean isRealItem() {
        return !UNSELECTED_ITEM_VALUE.equals(value);
    }
}
