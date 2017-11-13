package com.dreampany.framework.data.model;

import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by nuc on 8/22/2015.
 */
public class Tag extends Base {

    private String value;
    private long count;

    public Tag(){}

    public Tag(String value) {
        this.value = value;
        this.count = 1;
    }

    public Tag(String value, long count) {
        this.value = value;
        this.count = count;
    }

    protected Tag(Parcel in) {
        super(in);
        value = in.readString();
        count = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(value);
        dest.writeLong(count);
    }

    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void setTag(Tag tag) {
        setValue(tag.value);
    }

    @Bindable
    public String getValue() {
        return this.value;
    }

    @Bindable
    public long getCount() {
        return count;
    }

    public void setValue(String value) {
        this.value = value;
        //notifyPropertyChanged(BR.value);
    }

    public void setCount(long count) {
        this.count = count;
    //    notifyPropertyChanged(BR.count);
    }
}
