package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.model.Translate;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface TranslateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Translate translate);

    @Query("select targetText from translate where source = :source and target = :target and sourceText = :sourceText limit 1")
    String getTargetText(String source, String target, String sourceText);

    @Query("select sourceText from translate where source = :source and target = :target and targetText = :targetText limit 1")
    String getSourceText(String source, String target, String targetText);

    @Update
    void update(Translate translate);

    @Delete
    void delete(Translate translate);
}
