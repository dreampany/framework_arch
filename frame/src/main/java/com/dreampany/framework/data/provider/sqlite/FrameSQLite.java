package com.dreampany.framework.data.provider.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dreampany.framework.data.enums.Type;
import com.dreampany.framework.data.model.Point;
import com.dreampany.framework.data.model.Session;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nuc on 5/7/2017.
 */

public class FrameSQLite extends SQLite {

    private static FrameSQLite instance;
    private FrameResolver resolver;

    private FrameSQLite(Context context, String name, int version, String[] createTables) {
        super(context, name, version, createTables);
    }

    private FrameSQLite(Context context) {
        this(
                context,
                FrameSchema.Database.name,
                FrameSchema.Database.version,
                FrameSchema.CreateTables.createTables);
        resolver = new FrameResolver();
    }

    synchronized public static FrameSQLite onInstance(Context context) {
        if (instance == null) {
            instance = new FrameSQLite(context);
        }
        return instance;
    }

    synchronized public static void closeRef() {
        instance = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
        if (newVersion <= oldVersion) return;
        switch (newVersion) {
            case 2:
            case 3:
            case 4:
                if (oldVersion < newVersion) {
                    drop(db);
                }
                break;
        }
        onCreate(db);
    }

    public void write(Session session) {
        String table = FrameSchema.Tables.TABLE_SESSION;
        String selection = FrameSchema.Columns.HASH + " = ?";
        String[] selectionArgs = new String[]{""};

        if (hasEntry(table, selection, selectionArgs)) {
           // session.setTime(TimeUtil.currentTime());
            ContentValues values = resolver.getContentValues(session);

            update(table, values, selection, selectionArgs);

        } else {
            long id = buildId(table, FrameSchema.Columns.ID);
           // session.setId(id);
           // session.setTime(TimeUtil.currentTime());
            ContentValues values = resolver.getContentValues(session);
            insert(table, null, values);
        }
    }

    public void write(Point point) {
        String table = FrameSchema.Tables.TABLE_POINT;
        String selection = FrameSchema.Columns.HASH + " = ? and " + FrameSchema.Columns.TYPE + " = ?";
        String[] selectionArgs = null;//{Long.toString(point.getHash()), point.getType().value()};

        if (hasEntry(table, selection, selectionArgs)) {
            point.setTime(TimeUtil.currentTime());
            ContentValues values = resolver.getContentValues(point);

            update(table, values, selection, selectionArgs);

        } else {
            long id = buildId(table, FrameSchema.Columns.ID);
            //point.setId(id);
            point.setTime(TimeUtil.currentTime());
            ContentValues values = resolver.getContentValues(point);
            insert(table, null, values);
        }
    }

    public void writeFlag(long hash, Type type) {
        String table = FrameSchema.Tables.TABLE_FLAG;
        String selection = FrameSchema.Columns.HASH + " = ? and " + FrameSchema.Columns.TYPE + " = ?";
        String[] selectionArgs = {Long.toString(hash), type.value()};

        ContentValues values = new ContentValues();

        DataUtil.setValue(values, FrameSchema.Columns.TIME, TimeUtil.currentTime());
        DataUtil.setValue(values, FrameSchema.Columns.HASH, hash);

        DataUtil.setValue(values, FrameSchema.Columns.TYPE, type.value());

        if (hasEntry(table, selection, selectionArgs)) {
            update(table, values, selection, selectionArgs);

        } else {
            long id = buildId(table, FrameSchema.Columns.ID);
            DataUtil.setValue(values, FrameSchema.Columns.ID, id);
            insert(table, null, values);
        }
    }

    public void write(long hash, Type type, Type subtype, String value) {
        String table = FrameSchema.Tables.TABLE_VALUE;
        String selection = FrameSchema.Columns.HASH + " = ? and " + FrameSchema.Columns.TYPE + " = ? and " + FrameSchema.Columns.SUBTYPE + " = ? and " + FrameSchema.Columns.VALUE + " = ?";
        String[] selectionArgs = {Long.toString(hash), type.value(), subtype.value(), value};

        ContentValues values = new ContentValues();

        DataUtil.setValue(values, FrameSchema.Columns.TIME, TimeUtil.currentTime());
        DataUtil.setValue(values, FrameSchema.Columns.HASH, hash);

        DataUtil.setValue(values, FrameSchema.Columns.TYPE, type.value());
        DataUtil.setValue(values, FrameSchema.Columns.SUBTYPE, subtype.value());
        DataUtil.setValue(values, FrameSchema.Columns.VALUE, value);

        if (hasEntry(table, selection, selectionArgs)) {
            update(table, values, selection, selectionArgs);
        } else {
            long id = buildId(table, FrameSchema.Columns.ID);
            DataUtil.setValue(values, FrameSchema.Columns.ID, id);
            insert(table, null, values);
        }
    }

    public boolean isFlagged(long hash, Type type) {
        String table = FrameSchema.Tables.TABLE_FLAG;
        String selection = FrameSchema.Columns.HASH + " = ? and " + FrameSchema.Columns.TYPE + " = ?";
        String[] selectionArgs = {Long.toString(hash), type.value()};
        return hasEntry(table, selection, selectionArgs);
    }

    public boolean isStated(long hash, Type type, String state) {
        String table = FrameSchema.Tables.TABLE_VALUE;
        String selection = FrameSchema.Columns.HASH + " = ? and " + FrameSchema.Columns.TYPE + " = ? and " + FrameSchema.Columns.TYPE + " = ?";
        String[] selectionArgs = {Long.toString(hash), type.value(), state};
        return hasEntry(table, selection, selectionArgs);
    }

    public boolean deleteFlag(long hash, Type type) {
        String table = FrameSchema.Tables.TABLE_FLAG;
        String selection = FrameSchema.Columns.HASH + " = ? and " + FrameSchema.Columns.TYPE + " = ?";
        String[] selectionArgs = {Long.toString(hash), type.value()};
        return delete(table, selection, selectionArgs);
    }

    public boolean deleteState(long hash, Type type, String state) {
        String table = FrameSchema.Tables.TABLE_FLAG;
        String selection = FrameSchema.Columns.HASH + " = ? and " + FrameSchema.Columns.TYPE + " = ? and " + FrameSchema.Columns.TYPE + " = ?";
        String[] selectionArgs = {Long.toString(hash), type.value(), state};
        return delete(table, selection, selectionArgs);
    }

    public long getPoints() {
        String table = FrameSchema.Tables.TABLE_POINT;
        String pointQuery = String.format(Locale.ENGLISH,
                "select sum(%s) as %s from %s",
                FrameSchema.Columns.POINTS,
                FrameSchema.Columns.POINTS,
                table
        );
        String[] selectionArgs = null;
        Cursor cursor = rawQuery(pointQuery, selectionArgs);
        long points = 0L;
        if (hasCursor(cursor)) {
            if (cursor.moveToFirst()) {
                points = DataUtil.readLongValue(cursor, FrameSchema.Columns.POINTS, 0L);
            }
            closeCursor(cursor);
        }
        return points;
    }

    public long getPoints(Type pointType) {
        String table = FrameSchema.Tables.TABLE_POINT;
        String pointQuery = String.format(Locale.ENGLISH,
                "select sum(%s) as %s from %s where %s = ?",
                FrameSchema.Columns.POINTS,
                FrameSchema.Columns.POINTS,
                table,
                FrameSchema.Columns.TYPE
        );
        String[] selectionArgs = {pointType.toString()};
        Cursor cursor = rawQuery(pointQuery, selectionArgs);
        long points = 0L;
        if (hasCursor(cursor)) {
            if (cursor.moveToFirst()) {
                points = DataUtil.readLongValue(cursor, FrameSchema.Columns.POINTS, 0L);
            }
            closeCursor(cursor);
        }
        return points;
    }


    public boolean isEmpty(Type pointType) {
        return getPoints(pointType) == 0L;
    }


    public List<Long> getFlags(int page, int limitValue, Type type) {

        String table = FrameSchema.Tables.TABLE_FLAG;
        String[] projectionIn = FrameSchema.ProjectionIns.flagProjectionIn;
        String selection = FrameSchema.Columns.TYPE + " = ?";
        String[] selectionArgs = {type.value()};
        String sortOrder = FrameSchema.Columns.TIME + " desc";
        String groupBy = null;
        String limit = String.valueOf(page + limitValue);

        Cursor cursor = query(table, projectionIn, selection, selectionArgs, groupBy, sortOrder, limit);

        List<Long> items = null;

        if (hasCursor(cursor)) {
            if (cursor.moveToPosition(page)) {
                items = new ArrayList<>();
                do {
                    long item = DataUtil.readLongValue(cursor, FrameSchema.Columns.HASH, 0L);
                    items.add(item);
                } while (cursor.moveToNext());
            }
            closeCursor(cursor);
        }

        return items;
    }
}
