package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.model.State;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface StateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(State state);

    @Query("select count(*) from state")
    int count();

    @Query("select count(*) from state where id = :id and type = :type")
    int count(String id, String type);

    @Query("select count(*) from state where id = :id and type = :type and value = :value")
    int count(String id, String type, String value);

    @Query("select count(*) from state where type = :type and value in (:values)")
    int count(String type, String... values);

    @Query("select count(*) from state where id = :id and type = :type and value in (:values)")
    int count(String id, String type, String... values);

    @Query("select * from state")
    List<State> getAll();

    @Query("select * from state where id = :id and type = :type limit 1")
    State get(String id, String type);

    @Query("select * from state where type = :type and value in (:values)")
    List<State> gets(String type, String... values);

    @Query("select id from state where type = :type and value = :value limit 1")
    String getId(String type, String value);

    @Query("select value from state where id = :id and type = :type limit 1")
    String getValue(String id, String type);

    @Query("select id from state where type = :type and value in (:values)")
    List<String> getIds(String type, String... values);

    @Query("select id from state where type = :yesType and type not in (:noTypes) and value in (:values) limit 1")
    String getIdsNot(String yesType, String[] noTypes, String[] values);

    @Update
    void update(State state);

    @Delete
    void delete(State state);
}
