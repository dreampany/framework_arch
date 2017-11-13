package com.dreampany.framework.data.model;

import android.os.Parcel;

import com.dreampany.framework.data.enums.Type;

/**
 * Created by nuc on 4/10/2016.
 */
public enum Firebase implements Type {
    ADD, TAKE;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ordinal());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Firebase> CREATOR = new Creator<Firebase>() {

        public Firebase createFromParcel(Parcel in) {
            return Firebase.values()[in.readInt()];
        }

        public Firebase[] newArray(int size) {
            return new Firebase[size];
        }

    };

    @Override
    public boolean equals(Type type) {
        return type instanceof Firebase && compareTo((Firebase) type) == 0;
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
