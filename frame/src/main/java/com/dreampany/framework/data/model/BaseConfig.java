package com.dreampany.framework.data.model;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nuc on 12/6/2015.
 */
public abstract class BaseConfig extends BaseObservable implements Parcelable {

    private long id;
    private long time;
    private String hash;

    protected BaseConfig() {
    }

    protected BaseConfig(Parcel in) {
        id = in.readLong();
        time = in.readLong();
        hash = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(time);
        dest.writeString(hash);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public long getId() {
        return this.id;
    }

    public long getTime() {
        return this.time;
    }

/*    public Map<String, String> getTimestamp() {
        return ServerValue.TIMESTAMP;
    }*/

    public BaseConfig setId(long id) {
        this.id = id;
        return this;
    }

    public BaseConfig setTime(long time) {
        this.time = time;
        return this;
    }
}
