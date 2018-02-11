package com.dreampany.framework.data.enums;

import android.os.Parcel;

public enum Result implements Type {
    ERROR, SUCCESS;

    @Override
    public boolean equals(Type type) {
        if (Result.class.isInstance(type)) {
            Result item = (Result) type;
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

    public static final Creator<Result> CREATOR = new Creator<Result>() {

        public Result createFromParcel(Parcel in) {
            return Result.valueOf(in.readInt());
        }

        public Result[] newArray(int size) {
            return new Result[size];
        }

    };

    public static Result valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return ERROR;
            case 1:
                return SUCCESS;
            default:
                return null;
        }
    }
}