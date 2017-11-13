package com.dreampany.framework.data.provider.sqlite;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.dreampany.framework.BuildConfig;

import java.util.Locale;

/**
 * Created by air on 4/15/17.
 */

public interface FrameSchema extends SQLiteSchema {

    interface Database {
        String name = Iterables.getLast(Splitter.on(".").trimResults().split(BuildConfig.APPLICATION_ID));
        int version = BuildConfig.VERSION_CODE;
    }

    interface Tables  {
        String TABLE_SESSION = "session";
        String TABLE_POINT = "point";
        String TABLE_FLAG = "flag";
        String TABLE_VALUE = "value";
        String TABLE_TIME = "time";
    }

    interface Columns  {

        String ID = _ID;
        String COUNT = _COUNT;
        String TIME = "time";
        String HASH = "hash";

        String BEGIN_TIME = "begin_time";
        String END_TIME = "end_time";
        String DATE_TIME = "date_time";

        String POINTS = "points";
        String TYPE = "type";
        String SUBTYPE = "subtype";
        String VALUE = "value";
    }

    interface CreateTables {

        String createTableSession = String.format(Locale.ENGLISH,
                "create table if not exists %s (" +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +

                        "%s int not null default 0, " +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +

                        "unique (%s), " +
                        "primary key (%s) " +
                        ")",

                Tables.TABLE_SESSION,
                Columns.ID,
                Columns.TIME,
                Columns.HASH,

                Columns.BEGIN_TIME,
                Columns.END_TIME,
                Columns.DATE_TIME,

                Columns.ID,
                Columns.HASH
        );

        String createTablePoint = String.format(Locale.ENGLISH,
                "create table if not exists %s (" +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +

                        "%s int not null default 0, " +
                        "%s text not null, " +

                        "unique (%s), " +
                        "primary key (%s, %s) " +
                        ")",

                Tables.TABLE_POINT,
                Columns.ID,
                Columns.TIME,
                Columns.HASH,

                Columns.POINTS,
                Columns.TYPE,

                Columns.ID,
                Columns.HASH,
                Columns.TYPE
        );

        String createTableFlag = String.format(Locale.ENGLISH,
                "create table if not exists %s (" +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +

                        "%s text not null, " +

                        "primary key (%s) " +
                        ")",

                Tables.TABLE_FLAG,
                Columns.ID,
                Columns.TIME,
                Columns.HASH,

                Columns.TYPE,

                Columns.ID
        );

        String createTableValue = String.format(Locale.ENGLISH,
                "create table if not exists %s (" +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +

                        "%s text not null, " +
                        "%s text, " +
                        "%s text not null, " +

                        "primary key (%s) " +
                        ")",

                Tables.TABLE_VALUE,
                Columns.ID,
                Columns.TIME,
                Columns.HASH,

                Columns.TYPE,
                Columns.SUBTYPE,
                Columns.VALUE,

                Columns.ID
        );

        String createTableTime = String.format(Locale.ENGLISH,
                "create table if not exists %s (" +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +
                        "%s int not null default 0, " +

                        "%s text not null, " +
                        "%s text, " +
                        "%s int not null default 0, " +

                        "primary key (%s) " +
                        ")",

                Tables.TABLE_VALUE,
                Columns.ID,
                Columns.TIME,
                Columns.HASH,

                Columns.TYPE,
                Columns.SUBTYPE,
                Columns.TIME,


                Columns.ID
        );

        String[] createTables = new String[]{
                createTableSession,
                createTablePoint,
                createTableFlag,
                createTableValue,
                createTableTime
        };
    }

    interface ProjectionIns {

        String[] sessionProjectionIn = {
                Columns.ID,
                Columns.TIME,
                Columns.HASH,

                Columns.BEGIN_TIME,
                Columns.END_TIME,
                Columns.DATE_TIME
        };

        String[] pointProjectionIn = {
                Columns.ID,
                Columns.TIME,
                Columns.HASH,

                Columns.POINTS,
                Columns.TYPE
        };

        String[] flagProjectionIn = {
                Columns.ID,
                Columns.TIME,
                Columns.HASH,

                Columns.TYPE
        };

        String[] valueProjectionIn = {
                Columns.ID,
                Columns.TIME,
                Columns.HASH,

                Columns.TYPE,
                Columns.SUBTYPE,
                Columns.VALUE
        };

    }
}
