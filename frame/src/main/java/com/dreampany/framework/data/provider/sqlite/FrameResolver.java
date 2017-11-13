package com.dreampany.framework.data.provider.sqlite;

import android.content.ContentValues;

import com.dreampany.framework.data.model.BaseSerial;
import com.dreampany.framework.data.model.Point;
import com.dreampany.framework.data.model.Session;
import com.dreampany.framework.data.util.DataUtil;

/**
 * Created by nuc on 5/7/2017.
 */

public class FrameResolver extends SQLiteResolver {

    protected ContentValues getContentValues(Session entry) {
        ContentValues values = new ContentValues();

       // DataUtil.setValue(values, FrameSchema.Columns.ID, entry.getId());
       // DataUtil.setValue(values, FrameSchema.Columns.TIME, entry.getTime());
       // DataUtil.setValue(values, FrameSchema.Columns.HASH, entry.getHash());

        DataUtil.setValue(values, FrameSchema.Columns.BEGIN_TIME, entry.getBeginTime());
        DataUtil.setValue(values, FrameSchema.Columns.END_TIME, entry.getEndTime());
        DataUtil.setValue(values, FrameSchema.Columns.DATE_TIME, entry.getDatetime());

        return values;
    }

    protected ContentValues getContentValues(Point entry) {
        ContentValues values = new ContentValues();

        DataUtil.setValue(values, FrameSchema.Columns.ID, entry.getId());
        DataUtil.setValue(values, FrameSchema.Columns.TIME, entry.getTime());
        //DataUtil.setValue(values, FrameSchema.Columns.HASH, entry.getHash());

        DataUtil.setValue(values, FrameSchema.Columns.POINTS, entry.getPoints());
        //DataUtil.setValue(values, FrameSchema.Columns.TYPE, entry.getType().value());

        return values;
    }

    @Override
    protected String getDatabaseName() {
        return null;
    }

    @Override
    protected int getVersionCode() {
        return 0;
    }

    @Override
    protected String[] getTables() {
        return new String[0];
    }

    @Override
    public <T extends BaseSerial> boolean keepId(T entry, long id) {
        return false;
    }

    @Override
    public <T extends BaseSerial> boolean keepTime(T entry, long time) {
        return false;
    }

    @Override
    public <T extends BaseSerial> ContentValues getContentValues(T entry) {
        return null;
    }

    @Override
    public <T extends BaseSerial> T getEntry(T entry, ContentValues values) {
        return null;
    }

    @Override
    public String getRowId() {
        return null;
    }
}
