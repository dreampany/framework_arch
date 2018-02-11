package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum Priority implements Type {

    LOW(0), MEDIUM(1), HIGH(2), OMG(3);

    private final int level;

    Priority(int level) {
        this.level = level;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public boolean equals(Type type) {
        if (Priority.class.isInstance(type)) {
            Priority item = (Priority) type;
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

    public static final Creator<Priority> CREATOR = new Creator<Priority>() {

        public Priority createFromParcel(Parcel in) {
            return Priority.values()[in.readInt()];
        }

        public Priority[] newArray(int size) {
            return new Priority[size];
        }

    };
}
