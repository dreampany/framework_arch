package com.dreampany.framework.data.enums;

import android.os.Parcel;

/**
 * Created by nuc on 4/10/2016.
 */
public enum State implements Type {
    LOCAL, CLOUD;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<State> CREATOR = new Creator<State>() {

        public State createFromParcel(Parcel in) {
            return State.valueOf(in.readInt());
        }

        public State[] newArray(int size) {
            return new State[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof State && compareTo((State) type) == 0;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public String value() {
        return name();
    }

    public static State valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return LOCAL;
            case 1:
                return CLOUD;
            default:
                return null;
        }
    }
}
