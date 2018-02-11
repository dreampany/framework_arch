package com.dreampany.framework.data.manager;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;

import com.dreampany.framework.R;
import com.dreampany.framework.data.enums.NotifyCause;
import com.dreampany.framework.data.enums.Type;
import com.dreampany.framework.data.enums.Ui;
import com.dreampany.framework.data.event.NotifyEvent;

import br.com.goncalves.pugnotification.notification.PugNotification;

/**
 * Created by air on 10/23/17.
 */

public final class NotifyManager extends EventManager {

    private static final int NOTIFY_DEFAULT = 101;
    private static final String NOTIFY_IDENTIFIER = "101";

    private Bitmap icon;

    private static NotifyManager instance;

    private NotifyManager() {
    }

    synchronized public static NotifyManager onInstance() {
        if (instance == null) {
            instance = new NotifyManager();
        }
        return instance;
    }

    @Override
    protected boolean looping() throws InterruptedException {
        return false;
    }

    public void showNotification(Context context, String title, String message, Class<?> target) {
        showNotification(context, NOTIFY_DEFAULT, title, message, target);
    }

    public void showNotification(Context context, int notifyId, String title, String message, Class<?> target) {

/*        if (pref == null || !pref.isNotifyTimeExpired()) {
            return;
        }*/

        if (icon == null) {
            //icon = BitmapUtil.getBitmapFromResource(context.getResources(), R.mipmap.ic_launcher, 100, 100);
        }

        PugNotification.with(context).cancel(NOTIFY_DEFAULT);

        PugNotification.with(context)
                .load()
                .identifier(NOTIFY_DEFAULT)
                .title(title)
                .message(message)
                .smallIcon(R.mipmap.ic_launcher)
                .largeIcon(R.mipmap.ic_launcher)
                .flags(Notification.DEFAULT_ALL)
                .autoCancel(true)
                .click(target)
                .simple()
                .build();
    }

    public void postAlert(String id, String title) {
        postAlert(id, title, null, null);
    }

    public void postAlert(String id, String title, String comment, NotifyCause cause) {
        NotifyEvent event = new NotifyEvent(id);
        event.setUi(Ui.ALERT);
        event.setTitle(title);
        event.setComment(comment);
        event.setCause(cause);
        post(event);
    }

    public void postProgress(String id, String title, String comment) {
        NotifyEvent event = new NotifyEvent(id);
        event.setUi(Ui.PROGRESS);
        event.setTitle(title);
        event.setComment(comment);
        post(event);
    }

    public void postState(String id, String title, String comment, Type cause) {
        postState(id, title, comment, null, null, cause);
    }

    public void postState(String id, String title, String comment, Type type, Type subtype, Type cause) {
        NotifyEvent event = new NotifyEvent(id);
        event.setTitle(title);
        event.setComment(comment);
        event.setType(type);
        event.setSubtype(subtype);
        event.setCause(cause);
        event.setUi(Ui.STATE);
        post(event);
    }

}
