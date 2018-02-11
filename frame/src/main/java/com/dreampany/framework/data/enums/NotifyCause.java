package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum NotifyCause implements Type {
    OFFLINE, CONNECTING, CONNECTED, EMPTY, SUCCESS, ERROR, EXISTS;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotifyCause> CREATOR = new Creator<NotifyCause>() {

        public NotifyCause createFromParcel(Parcel in) {
            return NotifyCause.values()[in.readInt()];
        }

        public NotifyCause[] newArray(int size) {
            return new NotifyCause[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof NotifyCause && compareTo((NotifyCause) type) == 0;
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
