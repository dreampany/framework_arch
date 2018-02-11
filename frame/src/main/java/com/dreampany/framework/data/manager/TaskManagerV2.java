package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.enums.Type;
import com.dreampany.framework.data.enums.TaskPriority;
import com.dreampany.framework.data.model.Base;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.data.structure.SmartQueue;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.LogKit;

/**
 * Created by nuc on 12/24/2016.
 */

public class TaskManagerV2<K extends Task<?>> {

    private TaskManagerV2 parent;

    public void setParent(TaskManagerV2 parent) {
        this.parent = parent;
    }

    public TaskManagerV2 getParent() {
        return parent;
    }

    public void resolveTask(K task) throws InterruptedException {

    }

    protected final SmartQueue<K> taskQueue;
    volatile private Thread taskThread;

    public TaskManagerV2() {
        taskQueue = new SmartQueue<>();
    }

    public void start() {
    }

    public void stop() {
    }

    protected void insertTask(K task) {
        TaskPriority priority = (TaskPriority) task.getPriority();
        if (priority == null) {
            priority = TaskPriority.LOW;
        }
        switch (priority) {
            case LOW:
                taskQueue.insertLast(task);
                break;
            case MEDIUM:
            case HIGH:
                taskQueue.insertFirst(task);
                break;
        }
        //taskQueue.insertLast(task);
    }

    protected K get() {
        return taskQueue.pollFirst();
    }

    public void remove(K task) {
        taskQueue.remove(task);
    }

    private void clearQueue() {
        taskQueue.clear();
    }

    public int getCount() {
        return taskQueue.size();
    }

    public void postTask(K task) {
        postTask(null, task);
    }

    public void postTask(TaskManagerV2 parent, K task) {
        this.parent = parent;
        if (exists(task)) {
            return;
        }
        insertTask(task);
        resolveThread();
    }

    public void postTaskInFirst(K task) {
        if (exists(task)) {
            return;
        }
        taskQueue.insertFirst(task);
        resolveThread();
    }

    public void moveToFirst(K task) {
        remove(task);
        postTaskInFirst(task);
    }

    private boolean exists(K task) {
        for (Task<?> tTask : taskQueue) {
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

            K nextTask = get();
            if (nextTask == null) break;

            try {
                resolveTask(nextTask);
            } catch (InterruptedException e) {
                LogKit.error(toString() + " " + e.toString());
            }

        }
    }

   /* public void postCallback(final Task<T> task) {
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

    public void postCallback(final Task<I, T, S, K, P> task, final List<I> items) {
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

