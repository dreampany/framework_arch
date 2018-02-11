package com.dreampany.framework.data.enums;

import android.os.Parcel;

public enum TaskType implements Type {
    PRODUCE, RESOLVE, PUBLISH, DISCOVER;

    @Override
    public boolean equals(Type type) {
        if (TaskType.class.isInstance(type)) {
            TaskType item = (TaskType) type;
            return compareTo(item) == 0;
        }
        return false;
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

    public static final Creator<TaskType> CREATOR = new Creator<TaskType>() {

        public TaskType createFromParcel(Parcel in) {
            return TaskType.valueOf(in.readInt());
        }

        public TaskType[] newArray(int size) {
            return new TaskType[size];
        }

    };

    public static TaskType valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return PRODUCE;
            case 1:
                return RESOLVE;
            case 2:
                return PRODUCE;
            case 3:
                return DISCOVER;
            default:
                return null;
        }
    }
}