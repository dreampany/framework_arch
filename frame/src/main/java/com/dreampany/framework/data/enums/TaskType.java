package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum TaskType implements Type {

    READ, WRITE, SYNC, SEARCH, UI;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TaskType> CREATOR = new Creator<TaskType>() {

        public TaskType createFromParcel(Parcel in) {
            return TaskType.values()[in.readInt()];
        }

        public TaskType[] newArray(int size) {
            return new TaskType[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof TaskType && compareTo((TaskType) type) == 0;
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
