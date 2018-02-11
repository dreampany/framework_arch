package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;


/**
 * Created by air on 3/12/17.
 */

@Entity(indices = {@Index(value = {"keyword", "type", "subtype"}, unique = true)}, primaryKeys = {"keyword", "type", "subtype"})
public class Keyword extends BaseParcel {

    @NonNull
    private String keyword;
    @NonNull
    private String type;
    @NonNull
    private String subtype;

    @Ignore
    public Keyword() {
    }

    public Keyword(@NonNull String keyword) {
        this.keyword = keyword;
    }

    @Ignore
    private Keyword(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<Keyword> CREATOR = new Creator<Keyword>() {
        @Override
        public Keyword createFromParcel(Parcel in) {
            return new Keyword(in);
        }

        @Override
        public Keyword[] newArray(int size) {
            return new Keyword[size];
        }
    };

    @Override
    public boolean equals(Object inObject) {
        if (Keyword.class.isInstance(inObject)) {
            Keyword item = (Keyword) inObject;
            return keyword.equals(item.keyword) && type.equals(item.type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(keyword, type);
    }

    public void setKeyword(@NonNull String keyword) {
        this.keyword = keyword;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setSubtype(@NonNull String subtype) {
        this.subtype = subtype;
    }

    @NonNull
    public String getKeyword() {
        return keyword;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getSubtype() {
        return subtype;
    }
}
