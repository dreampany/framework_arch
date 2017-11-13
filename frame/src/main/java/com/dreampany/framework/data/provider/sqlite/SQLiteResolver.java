package com.dreampany.framework.data.provider.sqlite;

import android.content.ContentValues;

import com.dreampany.framework.data.model.BaseSerial;

/**
 * Created by nuc on 1/9/2017.
 */

public abstract class SQLiteResolver {

    protected abstract String getDatabaseName();

    protected abstract int getVersionCode();

    protected abstract String[] getTables();

    public abstract <T extends BaseSerial> boolean keepId(T entry, long id);

    public abstract <T extends BaseSerial> boolean keepTime(T entry, long time);

    public abstract <T extends BaseSerial> ContentValues getContentValues(T entry);

    public abstract <T extends BaseSerial> T getEntry(T entry, ContentValues values);

    public abstract String getRowId();

    public long getTime() {
        return System.currentTimeMillis();
    }
}

