package com.dreampany.framework.data.provider.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.dreampany.framework.data.api.weather.model.Weather;

import java.util.Locale;

/**
 * Created by air on 10/17/17.
 */

@Database(entities = {Weather.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {
    public static final String DATABASE = "weather-db";
    private static volatile WeatherDatabase instance;

    public abstract WeatherDao weatherDao();

    synchronized public static WeatherDatabase onInstance(Context context) {
        if (instance == null) {
            instance = newInstance(context, false);
        }
        return instance;
    }

    public static WeatherDatabase newInstance(Context context, boolean memoryOnly) {
        Builder<WeatherDatabase> builder;

        if (memoryOnly) {
            builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), WeatherDatabase.class);
        } else {
            builder = Room.databaseBuilder(context.getApplicationContext(), WeatherDatabase.class, DATABASE);
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
