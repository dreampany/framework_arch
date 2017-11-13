package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Created by air on 5/11/17.
 */

@Entity(primaryKeys = {"id", "type"})
public class Point extends BaseSerial {

    @NonNull
    private String id;
    @NonNull
    private String type;
    private long points;
    private String comment;
    private long time;


    public Point() {
    }

    @Override
    public boolean equals(Object inObject) {
        if (Point.class.isInstance(inObject)) {
            Point point = (Point) inObject;
            return id.equals(point.id) && type.equals(point.type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode() ^ type.hashCode();
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getType() {
        return type;
    }

    public long getPoints() {
        return points;
    }

    public String getComment() {
        return comment;
    }

    public long getTime() {
        return time;
    }
}
