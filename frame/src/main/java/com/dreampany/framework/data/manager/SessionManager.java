package com.dreampany.framework.data.manager;

import android.content.Context;

import com.dreampany.framework.data.model.Session;
import com.dreampany.framework.data.provider.sqlite.FrameSQLite;
import com.dreampany.framework.data.util.TimeUtil;

/**
 * Created by nuc on 5/6/2017.
 */

public final class SessionManager {

    private static SessionManager manager;
    private Session session;

    private SessionManager() {
    }

    synchronized public static SessionManager onInstance() {
        if (manager == null) {
            manager = new SessionManager();
        }
        return manager;
    }

    public Session trackSession(Context context) {
        if (session == null) {
            session = new Session();
            session.setBeginTime(TimeUtil.currentTime());
            session.setDatetime(TimeUtil.startOfDay());
        }

        session.setEndTime(TimeUtil.currentTime());
        FrameSQLite.onInstance(context).write(session);

        return session;
    }



}
