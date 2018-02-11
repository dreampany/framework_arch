package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.model.Point;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface PointDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void insert(Point point);

    @Query("select count(*) from point")
    int count();

    @Query("select count(*) from point where type = :type")
    int count(String type);

    @Query("select * from point")
    List<Point> getAll();

    @Query("select * from point where id = :id and type = :type limit 1")
    Point get(String id, String type);

    @Query("select * from point where type = :type")
    List<Point> getPoints(String type);

    @Query("select sum(points) from point")
    int getPoints();

    @Query("select sum(points) from point where points < 0")
    int getUsedPoints();

    @Query("select sum(points) from point where type = :type and subtype = :subtype and points < 0")
    int getUsedPoints(String type, String subtype);

    @Query("select sum(points) from point where points > 0")
    int getEarnedPoints();

    @Query("select sum(points) from point where type = :type and subtype = :subtype and points > 0")
    int getEarnedPoints(String type, String subtype);

    @Update
    void update(Point point);

    @Delete
    void delete(Point point);
}
