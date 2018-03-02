package com.dreampany.framework.data.provider.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.dreampany.framework.data.model.Category;
import com.dreampany.framework.data.model.Flag;
import com.dreampany.framework.data.model.History;
import com.dreampany.framework.data.model.Keyword;
import com.dreampany.framework.data.model.Language;
import com.dreampany.framework.data.model.Point;
import com.dreampany.framework.data.model.Session;
import com.dreampany.framework.data.model.State;
import com.dreampany.framework.data.model.Tag;
import com.dreampany.framework.data.model.Translate;
import com.dreampany.framework.data.model.User;

import java.util.Locale;

/**
 * Created by air on 10/17/17.
 */

@Database(entities = {Session.class, Point.class, State.class, Flag.class, Language.class, Translate.class, History.class, Category.class, Tag.class, Keyword.class, User.class}, version = 5)
public abstract class FrameDatabase extends RoomDatabase {
    public static final String DATABASE = "frame-db";
    private static volatile FrameDatabase instance;

    public abstract SessionDao sessionDao();

    public abstract PointDao pointDao();

    public abstract StateDao stateDao();

    public abstract FlagDao flagDao();

    public abstract LanguageDao languageDao();

    public abstract TranslateDao translateDao();

    public abstract HistoryDao historyDao();

    public abstract CategoryDao categoryDao();

    public abstract TagDao tagDao();

    public abstract UserDao userDao();

    synchronized public static FrameDatabase onInstance(Context context) {
        if (instance == null) {
            instance = newInstance(context, false);
        }
        return instance;
    }

    public static FrameDatabase newInstance(Context context, boolean memoryOnly) {
        Builder<FrameDatabase> builder;

        if (memoryOnly) {
            builder = Room.inMemoryDatabaseBuilder(context.getApplicationContext(), FrameDatabase.class);
        } else {
            builder = Room.databaseBuilder(context.getApplicationContext(), FrameDatabase.class, DATABASE);
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
