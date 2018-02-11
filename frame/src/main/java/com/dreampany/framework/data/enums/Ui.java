package com.dreampany.framework.data.enums;

import android.os.Parcel;


/**
 * Created by nuc on 12/3/2016.
 */

public enum Ui implements Type {
    TOAST, ALERT, PROGRESS, STATE;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ui> CREATOR = new Creator<Ui>() {

        public Ui createFromParcel(Parcel in) {
            return Ui.values()[in.readInt()];
        }

        public Ui[] newArray(int size) {
            return new Ui[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return this == type && type instanceof Ui && compareTo((Ui) type) == 0;
    }

    @Override
    public int ordinalValue() {
        return ordinal();
    }

    @Override
    public String value() {
        return name();
    }

    public static Ui valueOf(int ordinal) {
        switch (ordinal) {
            case 0:
                return TOAST;
            case 1:
                return ALERT;
            case 2:
                return PROGRESS;
            case 3:
                return STATE;
            default:
                return null;
        }
    }
}
