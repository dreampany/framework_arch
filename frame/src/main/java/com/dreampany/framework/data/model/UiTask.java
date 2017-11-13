package com.dreampany.framework.data.model;

import com.dreampany.framework.data.enums.Type;

/**
 * Created by nuc on 12/21/2016.
 */

public class UiTask<T> extends Task<T> {

    public UiTask() {
    }

    public UiTask(T item) {
        super(item);
    }

    public UiTask(T item, Type itemType) {
        super(item);
        setItemType(itemType);
    }
}
