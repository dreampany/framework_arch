package com.dreampany.framework.data.enums;

import android.os.Parcel;

/**
 * Created by nuc on 4/10/2016.
 */
public enum AdType implements Type {
    BANNER;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdType> CREATOR = new Creator<AdType>() {

        public AdType createFromParcel(Parcel in) {
            return AdType.valueOf(in.readInt());
        }

        public AdType[] newArray(int size) {
            return new AdType[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof AdType && compareTo((AdType) type) == 0;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public String value() {
        return name();
    }

    public static AdType valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return BANNER;
            default:
                return null;
        }
    }
}
