package com.dreampany.framework.data.model;

import android.databinding.Bindable;
import android.os.Parcel;

import com.dreampany.framework.data.util.DataUtil;

/**
 * Created by nuc on 2/24/2017.
 */

public abstract class Message extends BaseCloud {

    private String address;

    public Message() {
    }

/*    public Message(String address) {
        this();
        setAddress(address);
    }*/

    protected Message(Parcel in) {
        super(in);
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(address);
    }

    @Override
    public boolean equals(Object inObject) {
        return inObject instanceof Message &&
                DataUtil.equals(address, ((Message) inObject).address) &&
                super.equals(inObject);
    }

    public void setAddress(String address) {
        //setHash(DataUtil.getSha256(address));
        this.address = address;
        //notifyPropertyChanged(BR.value);
    }

    @Bindable
    public String getAddress() {
        return address;
    }
}
