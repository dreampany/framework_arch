package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.model.Task;

/**
 * Created by air on 7/1/17.
 */

public class DataManager extends TaskManagerV3 {

    private static DataManager instance;

    private DataManager() {
    }

    synchronized public static DataManager onInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public void resolveTask(Task task) throws InterruptedException {
        super.resolveTask(task);
        // this task is being processed in background
        task.process();
    }
}
