package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum Level implements Type {

    EASY(0), MEDIUM(1), HARD(2);

    private final int level;

    Level(int level) {
        this.level = level;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public boolean equals(Type type) {
        if (Level.class.isInstance(type)) {
            Level item = (Level) type;
            return compareTo(item) == 0;
        }
        return false;
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

    public static final Creator<Level> CREATOR = new Creator<Level>() {

        public Level createFromParcel(Parcel in) {
            return Level.values()[in.readInt()];
        }

        public Level[] newArray(int size) {
            return new Level[size];
        }

    };
}
