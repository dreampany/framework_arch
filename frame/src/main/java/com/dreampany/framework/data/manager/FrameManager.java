package com.dreampany.framework.data.manager;

import android.content.Context;

import com.dreampany.framework.data.model.Flag;
import com.dreampany.framework.data.model.History;
import com.dreampany.framework.data.model.Point;
import com.dreampany.framework.data.model.Translate;
import com.dreampany.framework.data.provider.room.CategoryDao;
import com.dreampany.framework.data.provider.room.FlagDao;
import com.dreampany.framework.data.provider.room.FrameDatabase;
import com.dreampany.framework.data.provider.room.HistoryDao;
import com.dreampany.framework.data.provider.room.PointDao;
import com.dreampany.framework.data.provider.room.StateDao;
import com.dreampany.framework.data.provider.room.TranslateDao;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.TimeUtil;

import java.util.List;
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
        if (context == null) {
            throw new NullPointerException();
        }
        this.context = context.getApplicationContext();
        database = FrameDatabase.onInstance(context);
        executor = Executors.newCachedThreadPool();
    }

    synchronized public static FrameManager onInstance(Context context) {
        if (instance == null) {
            instance = new FrameManager(context);
        }
        return instance;
    }

    public PointDao pointDao() {
        return database.pointDao();
    }

    public StateDao stateDao() {
        return database.stateDao();
    }

    public FlagDao flagDao() {
        return database.flagDao();
    }

    public TranslateDao translateDao() {
        return database.translateDao();
    }

    public HistoryDao historyDao() {
        return database.historyDao();
    }

    public CategoryDao categoryDao() {
        return database.categoryDao();
    }

    public boolean hasFlag(String id, String type, String subtype) {
        return database.flagDao().count(id, type, subtype) > 0;
    }

    public void insertFlag(Flag flag) {
        database.flagDao().insert(flag);
    }

    public void deleteFlag(Flag flag) {
        database.flagDao().delete(flag);
    }

    public Translate getTranslate(String source, String target, String sourceText) {

/*        Translate translate = database.translateDao().getTarget(source, target, sourceText);
        if (translate == null) {
            translate = database.translateDao().getSource(source, target, sourceText);
        }*/
        return database.translateDao().getTarget(source, target, sourceText);
    }

    public void trackPoints(String id, String type, String subtype, int points, String comment) {
        executor.execute(() -> {
            Point point = new Point();
            point.setId(id);
            point.setType(type);
            point.setSubtype(subtype);
            point.setPoints(points);
            point.setComment(comment);
            point.setTime(TimeUtil.currentTime());
            database.pointDao().insert(point);
        });
    }

    public void produceHistory(String type) {
        executor.execute(() -> {
            List<History> items = database.historyDao().getItems(type);
            if (!DataUtil.isEmpty(items)) {
                EventManager.post(items);
            }
        });
    }

    public void insertHistory(History history) {
        executor.execute(() -> {
            database.historyDao().insert(history);
        });
    }


/*    public void storeLanguage(int codeArray, int nameArray) {
        String[] codes = TextUtil.getStringArray(context, codeArray);
        String[] names = TextUtil.getStringArray(context, nameArray);


    }*/

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
