package com.dreampany.framework.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.dreampany.framework.data.enums.Type;

/**
 * Created by nuc on 11/16/2016.
 */

public class CloudTask<T extends Base, X extends Type, Y extends Type, S extends Type> extends Task<T, X, Y, S> {

    public T item;
    public boolean autoSync;
    public boolean persistent;

    public CloudTask() {
    }

    @Override
    protected CloudTask clone() throws CloneNotSupportedException {
        return (CloudTask) super.clone();
    }

    public CloudTask copy() {
        try {
            CloudTask copiedTask = clone();
            return copiedTask;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
