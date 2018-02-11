package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.enums.Type;
import com.dreampany.framework.data.model.BaseParcel;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.data.structure.SmartQueue;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.LogKit;

/**
 * Created by nuc on 12/24/2016.
 */

public abstract class TaskManager<T extends BaseParcel, X extends Type> extends Manager {

    private TaskManager parent;

    public void setParent(TaskManager parent) {
        this.parent = parent;
    }

    public TaskManager getParent() {
        return parent;
    }

    public void resolveTask(Task<T> task) throws InterruptedException {

    }

    protected final SmartQueue<Task<T>> taskQueue;
    volatile private Thread taskThread;

    public TaskManager() {
        taskQueue = new SmartQueue<>();
    }

    public void start() {
    }

    public void stop() {
    }

    protected void insertTask(Task<T> task) {
    }

    protected Task<T> get() {
        return taskQueue.pollFirst();
    }

    public void remove(Task<T> task) {
        taskQueue.remove(task);
    }

    private void clearQueue() {
        taskQueue.clear();
    }

    public int getCount() {
        return taskQueue.size();
    }

    public void postTask(Task<T> task) {
        postTask(null, task);
    }

    public void postTask(TaskManager parent, Task<T> task) {
        this.parent = parent;
        if (exists(task)) {
            return;
        }
        insertTask(task);
        resolveThread();
    }

    public void postTaskInFirst(Task<T> task) {
        if (exists(task)) {
            return;
        }
        taskQueue.insertFirst(task);
        resolveThread();
    }

    public void moveToFirst(Task<T> task) {
        remove(task);
        postTaskInFirst(task);
    }

    private boolean exists(Task<T> task) {
        for (Task<T> tTask : taskQueue) {
            if (tTask.equals(task)) {
                return true;
            }
        }
        return false;
    }

    private void resolveThread() {
        if (AndroidUtil.needThread(taskThread)) {
            taskThread = AndroidUtil.createThread(threadRunnable);
            taskThread.start();
        }
    }

    private final Runnable threadRunnable = new Runnable() {
        @Override
        public void run() {
            keepRunning();
        }
    };

    private void keepRunning() {

        while (true) {

            Task<T> nextTask = get();
            if (nextTask == null) break;

            try {
                resolveTask(nextTask);
            } catch (InterruptedException e) {
                LogKit.error(toString() + " " + e.toString());
            }

        }
    }

   /* public void postCallback(final Task<T, X> task) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Task.Callback callback = task.getCallback();
                if (callback != null) {
                    callback.onTask(task);
                }
            }
        };

        AndroidUtil.post(runnable);
    }

    public void postCallback(final Task<I, T, S, X, P> task, final List<I> items) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Task.Callback callback = task.getCallback();
                if (callback != null) {
                    callback.onData(task, items);
                }
            }
        };

        AndroidUtil.post(runnable);
    }*/
}

