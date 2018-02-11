package com.dreampany.framework.data.event;

import com.dreampany.framework.data.enums.Ui;
import com.dreampany.framework.data.model.Notify;

/**
 * Created by nuc on 6/13/2017.
 */

public class NotifyEvent extends Event<Notify> {

    private String id;
    private String title;
    private String comment;

    public NotifyEvent(String id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public boolean isToast() {
        return ui == Ui.TOAST;
    }

    public boolean isAlert() {
        return ui == Ui.ALERT;
    }

    public boolean isProgress() {
        return ui == Ui.PROGRESS;
    }

    public boolean isState() {
        return ui == Ui.STATE;
    }
}
