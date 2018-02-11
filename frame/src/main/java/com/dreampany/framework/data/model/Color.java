package com.dreampany.framework.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nuc on 1/24/2016.
 */
public class Color extends BaseParcel {

    private int colorPrimaryId;
    private int colorPrimaryDarkId;
    private int colorAccentId;

    public Color(int colorPrimaryId, int colorPrimaryDarkId) {
        this.colorPrimaryId = colorPrimaryId;
        this.colorPrimaryDarkId = colorPrimaryDarkId;
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

    public int getColorPrimaryId() {
        return colorPrimaryId;
    }

    public int getColorPrimaryDarkId() {
        return colorPrimaryDarkId;
    }

    public int getColorAccentId() {
        return colorAccentId;
    }

    public Color setColorPrimaryId(int colorPrimaryId) {
        this.colorPrimaryId = colorPrimaryId;
        //  notifyPropertyChanged(BR.colorPrimaryDarkId);
        return this;
    }

    public Color setColorPrimaryDarkId(int colorPrimaryDarkId) {
        this.colorPrimaryDarkId = colorPrimaryDarkId;
        //  notifyPropertyChanged(BR.colorPrimaryDarkId);
        return this;
    }

    public Color setColorAccentId(int colorAccentId) {
        this.colorAccentId = colorAccentId;
        return this;
    }
}
