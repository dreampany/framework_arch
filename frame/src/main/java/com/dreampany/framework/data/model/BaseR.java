package com.dreampany.framework.data.model;

import com.google.firebase.database.Exclude;

/**
 * Created by nuc on 12/6/2015.
 */
public abstract class BaseR /*extends RealmObject*/ {

    private long id;
    private long time;
    private long hash;

    public BaseR() {
    }

    @Override
    public boolean equals(Object inObject) {
        return inObject instanceof BaseR && hash == ((BaseR) inObject).hash;
    }

    @Exclude
    public long getId() {
        return this.id;
    }

    @Exclude
    public long getTime() {
        return this.time;
    }

    @Exclude
    public long getHash() {
        return hash;
    }

    public BaseR setId(long id) {
        this.id = id;
        return this;
    }

    public BaseR setTime(long time) {
        this.time = time;
        return this;
    }

    public BaseR setHash(long hash) {
        this.hash = hash;
        return this;
    }
}
