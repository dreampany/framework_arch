package com.dreampany.framework.data.manager;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.dreampany.framework.data.enums.Type;
import com.dreampany.framework.data.model.Flag;
import com.dreampany.framework.data.model.Point;
import com.dreampany.framework.data.provider.room.FlagDao;
import com.dreampany.framework.data.provider.room.FrameDatabase;
import com.dreampany.framework.data.provider.room.HistoryDao;
import com.dreampany.framework.data.provider.room.StateDao;
import com.dreampany.framework.data.provider.sqlite.FrameSQLite;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.TextUtil;
import com.dreampany.framework.data.util.TimeUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by air on 10/18/17.
 */

public final class FrameManager {

    private static FrameManager instance;
    private final Context context;
    private final FrameDatabase database;
    private Executor executor;

    private FrameManager(Context context) {
        this.context = context.getApplicationContext();
        database = Room.databaseBuilder(this.context, FrameDatabase.class, FrameDatabase.DATABASE).build();
        executor = Executors.newCachedThreadPool();
    }

    synchronized public static FrameManager onInstance(Context context) {
        if (instance == null) {
            instance = new FrameManager(context);
        }
        return instance;
    }

    public StateDao stateDao() {
        return database.stateDao();
    }

    public FlagDao flagDao() {
        return database.flagDao();
    }

    public HistoryDao historyDao() {
        return database.historyDao();
    }

    public boolean isFlagged(String id, String type) {
        return database.flagDao().count(id, type) > 0;
    }

    public void insertFlag(Flag flag) {
        database.flagDao().insert(flag);
    }

    public void deleteFlag(Flag flag) {
        database.flagDao().delete(flag);
    }

    public String getTranslation(String source, String target, String text) {
        text = text.toLowerCase();
        String result = database.translateDao().getTargetText(source, target, text);
        if (DataUtil.isEmpty(result)) {
            result = database.translateDao().getSourceText(target, source, text);
        }
        return result;
    }

    public void trackPoints(String id, String type, long points, String comment) {
        executor.execute(() -> {
            Point point = new Point();
            point.setId(id);
            point.setType(type);
            point.setPoints(points);
            point.setComment(comment);
            point.setTime(TimeUtil.currentTime());
            database.pointDao().insert(point);
        });
    }

/*    public long getAvailablePoints(Context context) {
        long totalPoints = FrameSQLite.onInstance(context).getPoints();
        long usedPoints = FramePref.onInstance(context).getLong(PointManager.usedPoints);
        return totalPoints - usedPoints;
    }

    public long getPoints(Context context, Type... types) {
        long points = 0L;
        for (Type type : types) {
            points += FrameSQLite.onInstance(context).getPoints(type);
        }
        return points;
    }*/
}
