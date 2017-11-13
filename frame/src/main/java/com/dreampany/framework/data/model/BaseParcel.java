package com.dreampany.framework.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nuc on 12/17/2016.
 */

public abstract class BaseParcel extends BaseSerial implements Parcelable {

    protected BaseParcel() {}

    protected BaseParcel(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
