package com.dreampany.framework.data.enums;

import android.os.Parcel;

/**
 * Created by nuc on 4/10/2016.
 */
public enum PointSubtype implements Type {
    ADD, SUB;

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof PointSubtype && compareTo((PointSubtype) type) == 0;
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

    public static final Creator<PointSubtype> CREATOR = new Creator<PointSubtype>() {

        public PointSubtype createFromParcel(Parcel in) {
            return PointSubtype.valueOf(in.readInt());
        }

        public PointSubtype[] newArray(int size) {
            return new PointSubtype[size];
        }

    };

    public static PointSubtype valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return ADD;
            case 1:
                return SUB;
            default:
                return null;
        }
    }
}
