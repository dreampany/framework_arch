package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.model.Language;
import com.dreampany.framework.data.model.Point;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface LanguageDao {
    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void insert(Language language);

    @Query("select count(*) from language")
    int count();

    @Query("select count(*) from language where code = :code")
    int count(String code);

    @Query("select * from language")
    List<Language> getAll();

    @Update
    void update(Language language);

    @Delete
    void delete(Language language);
}
