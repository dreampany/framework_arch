package com.dreampany.framework.data.model;

import com.dreampany.framework.data.enums.Type;

import java.util.Map;

/**
 * Created by air on 5/12/17.
 */

public class StoreTask<T extends BaseSerial> extends Task<T, Type, Type, Type> {

    private String collection;
    private Map<String, Object> data;


    public StoreTask() {

    }

    @Override
    public boolean equals(Object inObject) {
        if (StoreTask.class.isInstance(inObject)) {
            StoreTask task = (StoreTask) inObject;
            boolean equalCategory = collection == null || collection.equals(task.collection);
            return equalCategory && super.equals(inObject);
        }
        return false;
    }


    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getCollection() {
        return collection;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
