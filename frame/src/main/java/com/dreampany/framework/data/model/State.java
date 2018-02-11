package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

/**
 * Created by air on 10/18/17.
 */

@Entity(indices = {@Index(value = {"id", "type", "subtype"}, unique = true)}, primaryKeys = {"id", "type", "subtype"})
public class State extends BaseSerial {

    @NonNull
    private String id;
    @NonNull
    private String type;
    @NonNull
    private String subtype;
    @NonNull
    private String state;

    public State() {
    }

    @Override
    public boolean equals(Object inObject) {
        if (State.class.isInstance(inObject)) {
            State state = (State) inObject;
            return id.equals(state.id) && type.equals(state.type) && subtype.equals(state.subtype);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, type, subtype);
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setSubtype(@NonNull String subtype) {
        this.subtype = subtype;
    }

    public void setState(@NonNull String state) {
        this.state = state;
    }

    @NotNull
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

    @NonNull
    public String getState() {
        return state;
    }
}
