package com.dreampany.framework.data.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

/**
 * Created by nuc on 12/6/2015.
 */
public abstract class Base extends BaseParcel {

    @NonNull
    private String id;
    private long time;

    protected Base() {
    }

    protected Base(Parcel in) {
        id = in.readString();
        time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(time);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    public long getTime() {
        return this.time;
    }

    public Base setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    public Base setTime(long time) {
        this.time = time;
        return this;
    }
}
