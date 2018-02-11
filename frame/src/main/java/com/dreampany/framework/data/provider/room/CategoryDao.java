package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.model.Category;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Category category);

    @Query("select count(*) from category")
    int count();

    @Query("select count(*) from category where category = :category and type = :type")
    int count(String category, String type);

    @Query("select * from category")
    List<Category> getAll();

    @Query("select * from category where category = :category and type = :type and subtype = :subtype limit 1")
    Category get(String category, String type, String subtype);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);
}
