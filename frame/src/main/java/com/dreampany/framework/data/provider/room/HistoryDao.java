package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.model.History;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(History history);

    @Query("select value from history where type = :type")
    List<String> getValues(String type);

    @Query("select * from history where type = :type")
    List<History> getItems(String type);

    @Query("select * from history where type = :type limit :limit")
    List<History> getItems(String type, int limit);

    @Update
    void update(History history);

    @Delete
    void delete(History history);
}
