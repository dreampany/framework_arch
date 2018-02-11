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

@Entity(indices = {@Index(value = {"category", "type", "subtype"}, unique = true)}, primaryKeys = {"category", "type", "subtype"})
public class Category extends BaseParcel {

    @NonNull
    private String category;
    @NonNull
    private String type;
    @NonNull
    private String subtype;

    @Ignore
    public Category() {
    }

    public Category(@NonNull String category, @NonNull String type, @NonNull String subtype) {
        this.category = category;
        this.type = type;
        this.subtype = subtype;
    }

    @Ignore
    private Category(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
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
    public boolean equals(Object inObject) {
        if (Category.class.isInstance(inObject)) {
            Category item = (Category) inObject;
            return category.equals(item.category) && type.equals(item.type) && subtype.equals(item.subtype);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(category, type, subtype);
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setSubtype(@NonNull String subtype) {
        this.subtype = subtype;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getSubtype() {
        return subtype;
    }

    public String getId() {
        return String.valueOf(hashCode());
    }
}
