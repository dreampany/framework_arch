package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;

import com.dreampany.framework.data.util.DataUtil;
import com.google.common.primitives.Longs;

/**
 * Created by nuc on 5/6/2017.
 */

@Entity(primaryKeys = {"beginTime", "endTime"})
public class Session extends BaseSerial {

    private long beginTime;
    private long endTime;
    private long datetime;

    public Session() {
    }

    @Override
    public boolean equals(Object inObject) {
        if (Session.class.isInstance(inObject)) {
            Session session = (Session) inObject;
            return beginTime == session.beginTime
                    && datetime == session.datetime;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Longs.hashCode(beginTime) ^ Longs.hashCode(endTime);
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getDatetime() {
        return datetime;
    }

    public long getSessionTime() {
        return endTime - beginTime;
    }
}
