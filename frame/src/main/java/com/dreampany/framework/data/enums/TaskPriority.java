package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum TaskPriority implements Type {

    LOW, MEDIUM, HIGH;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TaskPriority> CREATOR = new Creator<TaskPriority>() {

        public TaskPriority createFromParcel(Parcel in) {
            return TaskPriority.values()[in.readInt()];
        }

        public TaskPriority[] newArray(int size) {
            return new TaskPriority[size];
        }

    };
    @Override
    public int ordinalValue() {
        return ordinal();
    }
    @Override
    public boolean equals(Type priority) {
        return this == priority && priority instanceof TaskPriority && compareTo((TaskPriority) priority) == 0;
    }

    @Override
    public String value() {
        return name();
    }
}
