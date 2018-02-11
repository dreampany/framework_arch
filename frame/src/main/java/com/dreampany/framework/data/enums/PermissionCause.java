package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum PermissionCause implements Type {
    NO_INTERNET, CONNECTING, CONNECTED, EMPTY;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PermissionCause> CREATOR = new Creator<PermissionCause>() {

        public PermissionCause createFromParcel(Parcel in) {
            return PermissionCause.values()[in.readInt()];
        }

        public PermissionCause[] newArray(int size) {
            return new PermissionCause[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof PermissionCause && compareTo((PermissionCause) type) == 0;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public String value() {
        return name();
    }
}
