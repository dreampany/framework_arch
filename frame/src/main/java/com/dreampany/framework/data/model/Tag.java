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

@Entity(indices = {@Index(value = {"tag", "type", "subtype"}, unique = true)}, primaryKeys = {"tag", "type", "subtype"})
public class Tag extends BaseParcel {

    @NonNull
    private String tag;
    @NonNull
    private String type;
    @NonNull
    private String subtype;

    @Ignore
    public Tag() {
    }

    public Tag(@NonNull String tag) {
        this.tag = tag;
    }

    @Ignore
    private Tag(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
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
    public boolean equals(Object inObject) {
        if (Tag.class.isInstance(inObject)) {
            Tag item = (Tag) inObject;
            return tag.equals(item.tag) && type.equals(item.type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tag, type);
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setSubtype(@NonNull String subtype) {
        this.subtype = subtype;
    }

    @NonNull
    public String getTag() {
        return tag;
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
