package com.dreampany.framework.data.model;/*
package com.ringbyte.framekit.data.model;

import android.databinding.Bindable;
import android.os.Parcel;

*/
/**
 * Created by nuc on 8/22/2015.
 *//*

public class Type extends Base {

    private String value;
    private long count;
    private boolean checked;

    public Type() {
    }

    public Type(String value) {
        this();
        setValue(value);
    }

    public Type(String value, boolean checked) {
        this(value);
        setChecked(checked);
    }

    protected Type(Parcel in) {
        super(in);
        value = in.readString();
        count = in.readLong();
        checked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(value);
        dest.writeLong(count);
        dest.writeByte((byte) (checked ? 1 : 0));
    }

    public static final Creator<Type> CREATOR = new Creator<Type>() {
        @Override
        public Type createFromParcel(Parcel in) {
            return new Type(in);
        }

        @Override
        public Type[] newArray(int size) {
            return new Type[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void setType(Type baseType) {
        setId(baseType.getId());
        setTime(baseType.getTime());
        setHash(baseType.getHash());
        setValue(baseType.value);
        setChecked(baseType.checked);
    }

    @Bindable
    public String getValue() {
        return value;
    }

    public long getCount() {
        return count;
    }

    @Bindable
    public boolean isChecked() {
        return checked;
    }

    public void setValue(String value) {
        setHash(HashUtil.md5Hash(value));
        this.value = value;
        notifyPropertyChanged(BR.value);
    }

    public void setCount(long count) {
        this.count = count;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        notifyPropertyChanged(BR.checked);
    }
}
*/
