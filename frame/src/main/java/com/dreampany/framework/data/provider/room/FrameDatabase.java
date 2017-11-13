package com.dreampany.framework.data.provider.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.dreampany.framework.data.model.Flag;
import com.dreampany.framework.data.model.History;
import com.dreampany.framework.data.model.Language;
import com.dreampany.framework.data.model.Point;
import com.dreampany.framework.data.model.Session;
import com.dreampany.framework.data.model.State;
import com.dreampany.framework.data.model.Translate;

/**
 * Created by air on 10/17/17.
 */

@Database(entities = {Session.class, Point.class, State.class, Flag.class, Language.class, Translate.class, History.class}, version = 1)
public abstract class FrameDatabase extends RoomDatabase {
    public static final String DATABASE = "frame-db";

    public abstract SessionDao sessionDao();

    public abstract PointDao pointDao();

    public abstract StateDao stateDao();

    public abstract FlagDao flagDao();

    public abstract TranslateDao translateDao();

    public abstract HistoryDao historyDao();
}
