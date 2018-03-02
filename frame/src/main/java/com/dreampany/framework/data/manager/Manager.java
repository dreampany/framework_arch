package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.enums.Priority;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.data.structure.SmartQueue;
import com.dreampany.framework.data.thread.Runner;

/**
 * Created by nuc on 5/20/2017.
 */

public abstract class Manager<T extends Task> extends Runner {

    private final SmartQueue<T> tasks;

    protected Manager() {
        tasks = new SmartQueue<>();
    }

    @Override
    public void run() {
        super.run();
    }

    public void clearAll() {
        tasks.clear();
        interrupt();
    }

    public void putTask(T task) {
        tasks.insertLast(task);
        startThread();
    }

    public void putTaskUniquely(T task) {
        tasks.insertLastUniquely(task);
        startThread();
    }

    public void putTaskPriority(T task) {
        Priority priority = task.getPriority();
        if (priority == Priority.HIGH || priority == Priority.OMG) {
            tasks.insertFirst(task);
        } else {
            tasks.insertLast(task);
        }
        startThread();
    }

    public T takeTask() {
        return tasks.takeFirst();
    }

    public T pollTask() {
        return tasks.pollFirst();
    }

    public int count() {
        return tasks.size();
    }
}
