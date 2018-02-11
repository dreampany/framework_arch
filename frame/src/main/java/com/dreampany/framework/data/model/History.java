package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

/**
 * Created by air on 10/18/17.
 */

@Entity(indices = {@Index(value = {"type", "value"}, unique = true)}, primaryKeys = {"type", "value"})
public class History extends BaseSerial {

    @NonNull
    private String type;
    @NonNull
    private String value;

    public History(@NotNull String type, @NotNull String value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object inObject) {
        if (History.class.isInstance(inObject)) {
            History history = (History) inObject;
            return type.equals(history.type) && value.equals(history.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, value);
    }

    public void setType(@NotNull String type) {
        this.type = type;
    }

    public void setValue(@NonNull String value) {
        this.value = value;
    }

    @NotNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getValue() {
        return value;
    }
}
