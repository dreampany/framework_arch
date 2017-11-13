package com.dreampany.framework.data.model;

import com.dreampany.framework.data.enums.Type;

import java.io.Serializable;

/**
 * Created by air on 10/22/17.
 */

public class Progress implements Serializable {

    private static final int TASK_BUILD = 1;


    private int progress;
    private Type type;
    private int task;

    public Progress(int progress, Type type) {
        this.progress = progress;
        this.type = type;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void makeBuildTask() {
        task = TASK_BUILD;
    }

    public int getProgress() {
        return progress;
    }

    public Type getType() {
        return type;
    }

    public boolean isBuildTask() {
        return task == TASK_BUILD;
    }

}
