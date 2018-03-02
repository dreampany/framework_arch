package com.dreampany.framework.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nuc on 1/24/2016.
 */
public class Color extends BaseParcel {

    private int primaryId;
    private int primaryDarkId;
    private int accentId;

    public Color(int primaryId, int primaryDarkId) {
        this.primaryId = primaryId;
        this.primaryDarkId = primaryDarkId;
    }

    protected Color(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Parcelable.Creator<Color> CREATOR = new Parcelable.Creator<Color>() {
        @Override
        public Color createFromParcel(Parcel in) {
            return new Color(in);
        }

        @Override
        public Color[] newArray(int size) {
            return new Color[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public int getPrimaryDarkId() {
        return primaryDarkId;
    }

    public int getAccentId() {
        return accentId;
    }

    public Color setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
        //  notifyPropertyChanged(BR.primaryDarkId);
        return this;
    }

    public Color setPrimaryDarkId(int primaryDarkId) {
        this.primaryDarkId = primaryDarkId;
        //  notifyPropertyChanged(BR.primaryDarkId);
        return this;
    }

    public Color setAccentId(int accentId) {
        this.accentId = accentId;
        return this;
    }
}
