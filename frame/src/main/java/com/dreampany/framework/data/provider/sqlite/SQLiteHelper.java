package com.dreampany.framework.data.provider.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Created by nuc on 12/3/2016.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private String[] createTables;

    protected SQLiteHelper(Context context, String name, int version, String[] createTables) {
        this(context, name, null, version);
        this.createTables = createTables;
    }

    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    private SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context.getApplicationContext(), name, factory, version, errorHandler);
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        //LogKit.verbose("SQLite onOpen() " + db.toString());

        if (!db.isReadOnly()) {
            onForeignKeys(db);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // LogKit.verbose("SQLite onCreate() " + db.toString());

        db.beginTransaction();

        try {
            for (String createTable : createTables) {
                db.execSQL(createTable);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long getId(SQLiteDatabase db, String table, String projectionInArg, String selection, String[] selectionArgs) {
        long id = 0;
        db = db == null ? getReadableDatabase() : db;
        db.beginTransaction();
        try {
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(table);
            String[] projectionIn = new String[]{projectionInArg};
            Cursor cursor = builder.query(db, projectionIn, selection, selectionArgs, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(projectionInArg));
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return id;
    }

    public long buildId(String table, String projectionInArg) {
        long id = 1;
        String[] projectionIn = new String[]{projectionInArg};
        String sortOrder = projectionInArg + " asc";
        Cursor cursor = query(table, projectionIn, null, null, null, sortOrder, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    long key = cursor.getInt(cursor.getColumnIndexOrThrow(projectionInArg));
                    if (key - id > 0) break;
                    id++;
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return id;
    }

    public boolean insert(String table, String nullColumnHack, ContentValues values) {
        boolean isSuccess;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            isSuccess = db.insert(table, nullColumnHack, values) != -1;
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    public boolean insert(String[] tables, String nullColumnHack, ContentValues[] values) {
        boolean isSuccess = true;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (int index = 0; index < tables.length; index++) {
                boolean inserted = db.insert(tables[index], nullColumnHack, values[index]) != -1;
                if (!inserted) {
                    isSuccess = false;
                    break;
                }
            }
            if (isSuccess)
                db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    public boolean update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        boolean isSuccess;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            isSuccess = db.update(table, values, whereClause, whereArgs) > 0;
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    public boolean update(String[] tables, ContentValues[] values, String[] whereClause, String[][] whereArgs) {
        boolean isSuccess = true;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (int index = 0; index < tables.length; index++) {
                boolean updated = db.update(tables[index], values[index], whereClause[index], whereArgs[index]) > 0;
                if (!updated) {
                    isSuccess = false;
                    break;
                }
            }
            if (isSuccess)
                db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    public Cursor query(String table, String[] projectionIn, String selection, String[] selectionArgs, String groupBy, String sortOrder, String limit) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(table);
        Cursor cursor;
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        try {
            cursor = builder.query(db, projectionIn, selection, selectionArgs, groupBy, null, sortOrder, limit);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    public boolean delete(String table, String whereClause, String[] whereArgs) {
        boolean isSuccess;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            isSuccess = db.delete(table, whereClause, whereArgs) > 0;
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return isSuccess;
    }

    public Cursor rawQuery(String query) {
        return rawQuery(null, query, null);
    }

    public Cursor rawQuery(String query, String[] selectionArgs) {
        return rawQuery(null, query, selectionArgs);
    }

    public Cursor rawQuery(SQLiteDatabase db, String query, String[] selectionArgs) {
        Cursor cursor;
        db = db == null ? getReadableDatabase() : db;
        db.beginTransaction();
        try {
            cursor = db.rawQuery(query, selectionArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    public long getEntryCount(String table) {

        long rowCount = 0;

        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        try {
            rowCount = DatabaseUtils.queryNumEntries(db, table);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return rowCount;
    }

    public long getEntryCount(String table, String selection) {

        long rowCount = 0;

        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        try {
            rowCount = DatabaseUtils.queryNumEntries(db, table, selection);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return rowCount;
    }

    public long getEntryCount(String table, String selection, String[] selectionArgs) {

        long rowCount = 0;

        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();
        try {
            rowCount = DatabaseUtils.queryNumEntries(db, table, selection, selectionArgs);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return rowCount;
    }

    private void onForeignKeys(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("PRAGMA foreign_keys = ON;");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void offForeignKeys(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("PRAGMA foreign_keys = OFF;");
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void drop(SQLiteDatabase db) {
        offForeignKeys(db);

        String projectionIn = "name";

        String tablesQuery = "select " + projectionIn + " from sqlite_master where type = ?";

        Cursor tablesCursor = rawQuery(db, tablesQuery, new String[]{"table"});

        if (tablesCursor == null) {
            onForeignKeys(db);
            return;
        }

        if (tablesCursor.moveToFirst()) {
            do {
                String tableName = tablesCursor.getString(tablesCursor.getColumnIndexOrThrow(projectionIn));
                drop(db, tableName);
            } while (tablesCursor.moveToNext());
        }

        tablesCursor.close();

        onForeignKeys(db);
    }

    public void drop(SQLiteDatabase db, String table) {
        db.beginTransaction();
        try {
            db.execSQL("drop table if exists " + table);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    protected boolean hasCursor(Cursor cursor) {

        if (cursor == null || cursor.isClosed()) return false;

        if (cursor.getCount() <= 0) {
            closeCursor(cursor);
            return false;
        }

        return true;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
