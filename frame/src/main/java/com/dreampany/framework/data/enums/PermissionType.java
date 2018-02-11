package com.dreampany.framework.data.enums;

import android.os.Parcel;

/**
 * Created by nuc on 4/10/2016.
 */
public enum PermissionType implements Type {
    LOCATION;

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof PermissionType && compareTo((PermissionType) type) == 0;
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

    public static final Creator<PermissionType> CREATOR = new Creator<PermissionType>() {

        public PermissionType createFromParcel(Parcel in) {
            return PermissionType.valueOf(in.readInt());
        }

        public PermissionType[] newArray(int size) {
            return new PermissionType[size];
        }

    };

    public static PermissionType valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return LOCATION;
            default:
                return null;
        }
    }
}
