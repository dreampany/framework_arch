package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.model.Flag;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface FlagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Flag flag);

    @Query("select count(*) from flag")
    int count();

    @Query("select count(*) from flag where id = :id and type = :type")
    int count(String id, String type);

    @Query("select * from flag")
    List<Flag> getAll();

    @Query("select * from flag where id = :id and type = :type limit 1")
    Flag get(String id, String type);

    @Query("select id from flag where type = :type")
    List<String> gets(String type);

    @Update
    void update(Flag flag);

    @Delete
    void delete(Flag flag);
}
