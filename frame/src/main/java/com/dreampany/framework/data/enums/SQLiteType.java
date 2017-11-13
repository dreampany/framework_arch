package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum SQLiteType implements Type {

    ADD, GET, RESOLVE;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SQLiteType> CREATOR = new Creator<SQLiteType>() {

        public SQLiteType createFromParcel(Parcel in) {
            return SQLiteType.values()[in.readInt()];
        }

        public SQLiteType[] newArray(int size) {
            return new SQLiteType[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof SQLiteType && compareTo((SQLiteType) type) == 0;
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
