package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum SQLiteQueryType implements Type {

    SINGLE, JOIN;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SQLiteQueryType> CREATOR = new Creator<SQLiteQueryType>() {

        public SQLiteQueryType createFromParcel(Parcel in) {
            return SQLiteQueryType.values()[in.readInt()];
        }

        public SQLiteQueryType[] newArray(int size) {
            return new SQLiteQueryType[size];
        }

    };    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof SQLiteQueryType && compareTo((SQLiteQueryType) type) == 0;
    }

    @Override
    public String value() {
        return name();
    }
}
