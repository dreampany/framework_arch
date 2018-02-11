package com.dreampany.framework.data.enums;

import android.os.Parcel;

/**
 * Created by nuc on 4/10/2016.
 */
public enum MissingType implements Type {
    PLAY_SERVICE;

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof MissingType && compareTo((MissingType) type) == 0;
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

    public static final Creator<MissingType> CREATOR = new Creator<MissingType>() {

        public MissingType createFromParcel(Parcel in) {
            return MissingType.valueOf(in.readInt());
        }

        public MissingType[] newArray(int size) {
            return new MissingType[size];
        }

    };

    public static MissingType valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return PLAY_SERVICE;
            default:
                return null;
        }
    }
}
