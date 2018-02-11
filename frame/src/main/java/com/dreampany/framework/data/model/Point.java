package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

/**
 * Created by air on 5/11/17.
 */

@Entity(indices = {@Index(value = {"id", "type", "subtype"}, unique = true)}, primaryKeys = {"id", "type", "subtype"})
public class Point extends Base {

    @NonNull
    private String id;
    @NonNull
    private String type;
    @NonNull
    private String subtype;
    private int points;
    private String comment;

    public Point() {
    }

    @Ignore
    public Point(@NonNull String id, @NonNull String type, @NonNull String subtype) {
        this.id = id;
        this.type = type;
        this.subtype = subtype;
    }

    @Ignore
    private Point(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

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

    public void setPoints(int points) {
        this.points = points;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public int getPoints() {
        return points;
    }

    public String getComment() {
        return comment;
    }
}
