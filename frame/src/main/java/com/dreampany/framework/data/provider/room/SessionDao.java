package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.model.Session;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface SessionDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void insert(Session session);

    @Query("select count(*) from session")
    int count();

    @Query("select * from session")
    List<Session> getAll();

    @Update
    void update(Session session);

    @Delete
    void delete(Session session);
}
