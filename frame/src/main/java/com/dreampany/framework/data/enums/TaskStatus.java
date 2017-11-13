package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum TaskStatus implements Type {

    FAIL, SUCCESS;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TaskStatus> CREATOR = new Creator<TaskStatus>() {

        public TaskStatus createFromParcel(Parcel in) {
            return TaskStatus.values()[in.readInt()];
        }

        public TaskStatus[] newArray(int size) {
            return new TaskStatus[size];
        }

    };

    @Override
    public boolean equals(Type status) {
        return this == status && status instanceof TaskStatus && compareTo((TaskStatus) status) == 0;
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
