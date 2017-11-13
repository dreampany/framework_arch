package com.dreampany.framework.data.enums;

import android.os.Parcel;

/**
 * Created by nuc on 4/10/2016.
 */
public enum Status implements Type {
    NEW, SAVED, ADDED, CHANGED, DELETED, MOVED, SENDING, SENT, RECEIVING, RECEIVED, ACTIVE, AWAY, FAILED, DISCOVERED, CONNECTED, SELF;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {

        public Status createFromParcel(Parcel in) {
            return Status.valueOf(in.readInt());
        }

        public Status[] newArray(int size) {
            return new Status[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof Status && compareTo((Status) type) == 0;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public String value() {
        return name();
    }

    public static Status valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return NEW;
            case 1:
                return SAVED;
            case 2:
                return ADDED;
            case 3:
                return CHANGED;
            case 4:
                return DELETED;
            case 5:
                return MOVED;
            case 6:
                return SENDING;
            case 7:
                return SENT;
            case 8:
                return RECEIVING;
            case 9:
                return RECEIVED;
            case 10:
                return ACTIVE;
            case 11:
                return AWAY;
            case 12:
                return FAILED;
            case 13:
                return DISCOVERED;
            case 14:
                return CONNECTED;
            case 15:
                return SELF;
            default:
                return null;
        }
    }
}
