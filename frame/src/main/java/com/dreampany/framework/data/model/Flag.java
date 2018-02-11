package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

/**
 * Created by air on 10/18/17.
 */

@Entity(indices = {@Index(value = {"id", "type", "subtype"}, unique = true)}, primaryKeys = {"id", "type", "subtype"})
public class Flag extends BaseSerial {

    @NonNull
    private String id;
    @NonNull
    private String type;
    @NonNull
    private String subtype;
    private int orderBy;

    public Flag() {

    }

    @Ignore
    public Flag(@NonNull String id, @NonNull String type, @NonNull String subtype) {
        this.id = id;
        this.type = type;
        this.subtype = subtype;
    }

    @Override
    public boolean equals(Object inObject) {
        if (Flag.class.isInstance(inObject)) {
            Flag flag = (Flag) inObject;
            return id.equals(flag.id) && type.equals(flag.type) && subtype.equals(flag.subtype);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, subtype);
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setSubtype(@NonNull String subtype) {
        this.subtype = subtype;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getSubtype() {
        return subtype;
    }

    public int getOrderBy() {
        return orderBy;
    }
}
