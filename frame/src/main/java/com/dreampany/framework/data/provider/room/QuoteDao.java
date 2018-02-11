package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dreampany.framework.data.model.Quote;

import java.util.List;

/**
 * Created by air on 10/17/17.
 */

@Dao
public interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Quote quote);

    @Query("select count(*) from quote where time = :time")
    int count(long time);

    @Query("select * from quote")
    List<Quote> getAll();

    @Query("select * from quote order by time desc limit 1")
    Quote getLatest();

    @Query("select * from quote where time >= :time limit 1")
    Quote getQuote(long time);

    @Query("select * from quote where author = :author")
    List<Quote> getQuotesByAuthor(String author);


    @Update
    void update(Quote quote);

    @Delete
    void delete(Quote quote);
}
