package com.dreampany.framework.data.model;

import android.os.Parcel;

/**
 * Created by nuc on 12/6/2015.
 */
public abstract class Base extends BaseParcel {

    private long time;

    protected Base() {
    }

    protected Base(Parcel in) {
        super(in);
        time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(time);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
