package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum SQLiteSelectionType implements Type {

    SINGLE, MULTIPLE;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SQLiteSelectionType> CREATOR = new Creator<SQLiteSelectionType>() {

        public SQLiteSelectionType createFromParcel(Parcel in) {
            return SQLiteSelectionType.values()[in.readInt()];
        }

        public SQLiteSelectionType[] newArray(int size) {
            return new SQLiteSelectionType[size];
        }

    };    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof SQLiteSelectionType && compareTo((SQLiteSelectionType) type) == 0;
    }

    @Override
    public String value() {
        return name();
    }
}
