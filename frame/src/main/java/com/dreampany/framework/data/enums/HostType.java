package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum HostType implements Type {

    SELF, REMOTE;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String value() {
        return name();
    }

    public static final Creator<HostType> CREATOR = new Creator<HostType>() {

        public HostType createFromParcel(Parcel in) {
            return HostType.values()[in.readInt()];
        }

        public HostType[] newArray(int size) {
            return new HostType[size];
        }

    };
    @Override
    public int ordinalValue() {
        return ordinal();
    }
    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof HostType && compareTo((HostType) type) == 0;
    }
}
