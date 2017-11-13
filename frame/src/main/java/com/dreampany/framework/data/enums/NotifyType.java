package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum NotifyType implements Type {

    ALERT, TOAST;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotifyType> CREATOR = new Creator<NotifyType>() {

        public NotifyType createFromParcel(Parcel in) {
            return NotifyType.values()[in.readInt()];
        }

        public NotifyType[] newArray(int size) {
            return new NotifyType[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof NotifyType && compareTo((NotifyType) type) == 0;
    }
    @Override
    public int ordinalValue() {
        return ordinal();
    }
    @Override
    public String value() {
        return name();
    }

    public static NotifyType valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return ALERT;
            default:
                return null;
        }
    }
}
