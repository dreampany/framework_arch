package com.dreampany.framework.data.model;

import android.databinding.Bindable;
import android.os.Parcel;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.Map;

/**
 * Created by nuc on 11/13/2016.
 */

public abstract class BaseCloud extends Base {

    private long cloudTime;

    protected BaseCloud() {
    }

    protected BaseCloud(Parcel in) {
        super(in);
        cloudTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(cloudTime);
    }

    @Override
    public boolean equals(Object inObject) {
        if (this == inObject) {
            return true;
        }
        if (!BaseCloud.class.isInstance(inObject)) {
            return false;
        }
        return super.equals(inObject);
    }

    protected void setBaseCloud(BaseCloud baseCloud) {
        setCloudTime(baseCloud.cloudTime);
    }

    @Exclude
    @Bindable
    public long getCloudTime() {
        return cloudTime;
    }

    public void setCloudTime(long cloudTime) {
        this.cloudTime = cloudTime;
    }

    public Map<String, String> getTimestamp() {
        return ServerValue.TIMESTAMP;
    }
}
