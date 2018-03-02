package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

/**
 * Created by air on 10/18/17.
 */

@Entity(indices = {@Index(value = {"id"}, unique = true)})
public class User extends BaseSerial {

    @NonNull
    @PrimaryKey
    private String id;
    private String name;


    public User(@NotNull String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object inObject) {
        if (User.class.isInstance(inObject)) {
            User item = (User) inObject;
            return id.equals(item.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
