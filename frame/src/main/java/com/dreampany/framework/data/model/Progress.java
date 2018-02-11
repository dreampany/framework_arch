package com.dreampany.framework.data.model;

import com.dreampany.framework.data.enums.Type;

import java.io.Serializable;

/**
 * Created by air on 10/22/17.
 */

public class Progress implements Serializable {

    private int progress;
    private int count;
    private int total;
    private Type type;

    public Progress(int progress, Type type) {
        this.progress = progress;
        this.type = type;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getProgress() {
        return progress;
    }

    public int getCount() {
        return count;
    }

    public int getTotal() {
        return total;
    }

    public Type getType() {
        return type;
    }
}
