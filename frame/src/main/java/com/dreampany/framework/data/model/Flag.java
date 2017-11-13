package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

/**
 * Created by air on 10/18/17.
 */

@Entity(indices = {@Index(value = {"id", "type"}, unique = true)}, primaryKeys = {"id", "type"})
public class Flag extends BaseSerial {

    @NonNull
    private String id;
    @NonNull
    private String type;

    public Flag(@NotNull String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object inObject) {
        if (Flag.class.isInstance(inObject)) {
            Flag flag = (Flag) inObject;
            return id.equals(flag.id) && type.equals(flag.type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode() ^ type.hashCode();
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getType() {
        return type;
    }
}
