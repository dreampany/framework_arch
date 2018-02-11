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

    @Query("select count(*) from state where type = :type and subtype = :subtype and state = :state")
    int count(String type, String subtype, String state);

    @Query("select count(*) from state where type = :type and subtype = :subtype and state in (:states)")
    int count(String type, String subtype, String... states);

    @Query("select count(*) from state where id = :id and type = :type and subtype = :subtype and state = :state")
    int count(String id, String type, String subtype, String state);

    @Query("select count(*) from state where id = :id and type = :type and subtype = :subtype and state in (:states)")
    int count(String id, String type, String subtype, String... states);

    @Query("select * from state")
    List<State> getAll();

    @Query("select * from state where id = :id and type = :type limit 1")
    State get(String id, String type);

    @Query("select * from state where type = :type and state in (:states)")
    List<State> gets(String type, String... states);

    @Query("select id from state where type = :type and state = :state limit 1")
    String getId(String type, String state);

    @Query("select state from state where id = :id and type = :type limit 1")
    String getValue(String id, String type);

    @Query("select id from state where type = :type and subtype = :subtype")
    List<String> getIds(String type, String subtype);

    @Query("select id from state where type = :type and subtype = :subtype and state in (:states)")
    List<String> getIds(String type, String subtype, String... states);

    @Query("select id from state where type = :type and subtype = :subtype and state = :state and state != :noState")
    List<String> getIds(String type, String subtype, String state, String noState);

    @Query("select id from state where type = :type and subtype = :subtype and state in (:states) limit 1")
    String getId(String type, String subtype, String... states);

    @Query("select id from state where type = :type and subtype = :subtype and state = :state and state != :noState limit 1")
    String getIdWithNo(String type, String subtype, String state, String noState);

    @Query("select id from state where type = :yesType and type not in (:noTypes) and state in (:states) limit 1")
    String getIdsNot(String yesType, String[] noTypes, String[] states);

    @Update
    void update(State state);

    @Delete
    void delete(State state);
}
