package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum Density implements Type {

    SINGLE, MULTIPLE, SIMPLE, FULL;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Density> CREATOR = new Creator<Density>() {

        public Density createFromParcel(Parcel in) {
            return Density.values()[in.readInt()];
        }

        public Density[] newArray(int size) {
            return new Density[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof Density && compareTo((Density) type) == 0;
    }

    @Override
    public String value() {
        return name();
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }
}
