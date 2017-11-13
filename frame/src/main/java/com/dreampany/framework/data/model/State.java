package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Created by air on 10/18/17.
 */

@Entity(indices = {@Index(value = {"id", "type"}, unique = true)}, primaryKeys = {"id", "type"})
public class State extends BaseSerial {

    @NonNull
    private String id;
    @NonNull
    private String type;
    @NonNull
    private String value;
    private long time;

    public State() {
    }

    @Override
    public boolean equals(Object inObject) {
        if (State.class.isInstance(inObject)) {
            State state = (State) inObject;
            return id.equals(state.id) && type.equals(state.type);
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

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setValue(@NonNull String value) {
        this.value = value;
    }

    public void setTime(long time) {
        this.time = time;
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
    public String getValue() {
        return value;
    }

    public long getTime() {
        return time;
    }
}
