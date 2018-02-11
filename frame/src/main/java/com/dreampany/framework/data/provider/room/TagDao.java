package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.model.Tag;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tag tag);

    @Query("select count(*) from tag")
    int count();

    @Query("select count(*) from tag where tag = :tag and type = :type")
    int count(String tag, String type);

    @Query("select * from tag")
    List<Tag> getAll();

    @Query("select * from tag where tag = :tag and type = :type limit 1")
    Tag get(String tag, String type);

    @Update
    void update(Tag tag);

    @Delete
    void delete(Tag tag);
}
