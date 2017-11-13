package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum Priority implements Type {

    LOW, MEDIUM, HIGH;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Priority> CREATOR = new Creator<Priority>() {

        public Priority createFromParcel(Parcel in) {
            return Priority.values()[in.readInt()];
        }

        public Priority[] newArray(int size) {
            return new Priority[size];
        }

    };
    @Override
    public int ordinalValue() {
        return ordinal();
    }
    @Override
    public boolean equals(Type priority) {
        return this == priority && priority instanceof Priority && compareTo((Priority) priority) == 0;
    }

    @Override
    public String value() {
        return name();
    }
}
