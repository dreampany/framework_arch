package com.dreampany.framework.data.model;/*
package com.ringbyte.framekit.data.model;

import android.databinding.Bindable;
import android.os.Parcel;

import com.ringbyte.english.BR;

*/
/**
 * Created by nuc on 8/22/2015.
 *//*

public class Category extends Base {

    private String title;
    private long count;
    private boolean checked;

    public Category(String title) {
        this.title = title;
    }

    public Category(String title, boolean checked) {
        this.title = title;
        this.checked = checked;
    }

    protected Category(Parcel in) {
        super(in);
        title = in.readString();
        count = in.readLong();
        checked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(title);
        dest.writeLong(count);
        dest.writeByte((byte) (checked ? 1 : 0));
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Category setCategory(Category category) {
        setTitle(category.title);
        setCount(category.count);

        setChecked(category.checked);
        return this;
    }

    @Bindable
    public String getTitle() {
        return this.title;
    }

    @Bindable
    public long getCount() {
        return count;
    }

    @Bindable
    public boolean isChecked() {
        return checked;
    }

    public Category setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
        return this;
    }

    public Category setCount(long count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
        return this;
    }

    public Category setChecked(boolean checked) {
        this.checked = checked;
        notifyPropertyChanged(BR.checked);
        return this;
    }
}
*/
