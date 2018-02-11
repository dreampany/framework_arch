package com.dreampany.framework.data.provider.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.dreampany.framework.data.model.Quote;

import java.util.Locale;

/**
 * Created by air on 10/17/17.
 */

@Database(entities = {Quote.class}, version = 1)
public abstract class QuoteDatabase extends RoomDatabase {
    public static final String DATABASE = "quote-db";
    private static volatile QuoteDatabase instance;

    public abstract QuoteDao quoteDao();

    synchronized public static QuoteDatabase onInstance(Context context) {
        if (instance == null) {
            instance = newInstance(context, false);
        }
        return instance;
    }

    public static QuoteDatabase newInstance(Context context, boolean memoryOnly) {
        Builder<QuoteDatabase> builder;

        if (memoryOnly) {
            builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), QuoteDatabase.class);
        } else {
            builder = Room.databaseBuilder(context.getApplicationContext(), QuoteDatabase.class, DATABASE);
        }

        return builder.fallbackToDestructiveMigration().build();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            String createCategory = String.format(Locale.ENGLISH,
                    "create table if not exists %s (" +
                            "%s text not null, " +
                            "%s text not null, " +
                            "primary key (%s, %s) " +
                            ")",

                    "category",
                    "category",
                    "type",
                    "category",
                    "type"
            );

            String createTag = String.format(Locale.ENGLISH,
                    "create table if not exists %s (" +
                            "%s text not null, " +
                            "%s text not null, " +
                            "primary key (%s, %s) " +
                            ")",

                    "tag",
                    "tag",
                    "type",
                    "tag",
                    "type"
            );

            //String addSubtypeInPoint = "alter table point add column last_update INTEGER";

            database.beginTransaction();
            try {
                //database.execSQL(createCategory);
                //database.execSQL(createTag);
                database.setTransactionSuccessful();
            } finally {
                database.endTransaction();
            }
        }
    };
}
