package com.dreampany.framework.data.api.swipe;

/**
 * Created by air on 11/7/17.
 */

public class Item {
    public String id;
    public String title;
    public String description;

    public Item(String id, String title, String description) {
       this.id = id;
       this.title = title;
       this.description = description;
    }

    @Override
    public boolean equals(Object in) {
        if (in instanceof Item) {
            Item item = (Item) in;
            return item.description.equals(description);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
