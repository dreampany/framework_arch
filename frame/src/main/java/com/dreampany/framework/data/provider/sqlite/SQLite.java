package com.dreampany.framework.data.provider.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by nuc on 1/9/2017.
 */

public class SQLite extends SQLiteHelper {

    protected SQLite(Context context, String name, int version, String[] createTables) {
        super(context, name, version, createTables);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean hasEntry(String table, String selection, String[] selectionArgs) {
        return getEntryCount(table, selection, selectionArgs) > 0;
    }

    public void export(Context context) {
        try {
            String database = getDatabaseName();
            String packageName = context.getPackageName();
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            FileChannel source = null;
            FileChannel destination = null;
            String currentDBPath = "/data/" + packageName + "/databases/" + database;
            //String backupDBPath = database;
            File currentDB = new File(data, currentDBPath);
            File backupDB = new File(sd, database);

            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

