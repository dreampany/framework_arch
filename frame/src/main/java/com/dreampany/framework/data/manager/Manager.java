package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.data.structure.SmartQueue;
import com.dreampany.framework.data.thread.Runner;

/**
 * Created by nuc on 5/20/2017.
 */

public abstract class Manager extends Runner {

    protected volatile boolean started;
    protected SmartQueue<Task> tasks;

    protected Manager() {
        tasks = new SmartQueue<>();
    }

    public void start() {
        super.start();
    }

    public void stop() {
        super.stop();
    }

    @Override
    public void run() {
        super.run();
    }

    public void putTask(Task task) {
        if (task.isHighPriority()) {
            tasks.insertFirst(task);
        } else {
            tasks.insertLast(task);
        }
        start();
    }
}
