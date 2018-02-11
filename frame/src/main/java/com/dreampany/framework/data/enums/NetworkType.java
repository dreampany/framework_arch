package com.dreampany.framework.data.enums;

import android.os.Parcel;

public enum NetworkType implements Type {
    WIFI, MOBILE;

    @Override
    public boolean equals(Type type) {
        if (NetworkType.class.isInstance(type)) {
            NetworkType item = (NetworkType) type;
            return compareTo(item) == 0;
        }
        return false;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public String value() {
        return name();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NetworkType> CREATOR = new Creator<NetworkType>() {

        public NetworkType createFromParcel(Parcel in) {
            return NetworkType.valueOf(in.readInt());
        }

        public NetworkType[] newArray(int size) {
            return new NetworkType[size];
        }

    };

    public static NetworkType valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return WIFI;
            case 1:
                return MOBILE;
            default:
                return null;
        }
    }
}