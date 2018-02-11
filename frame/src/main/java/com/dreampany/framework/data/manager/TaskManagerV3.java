package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.enums.TaskPriority;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.data.structure.SmartQueue;
import com.dreampany.framework.data.util.AndroidUtil;
import com.dreampany.framework.data.util.LogKit;

/**
 * Created by nuc on 12/24/2016.
 */

public class TaskManagerV3 {

    private TaskManagerV3 parent;

    public void setParent(TaskManagerV3 parent) {
        this.parent = parent;
    }

    public TaskManagerV3 getParent() {
        return parent;
    }

    public void resolveTask(Task task) throws InterruptedException {

    }

    protected final SmartQueue<Task> taskQueue;
    volatile private Thread taskThread;

    public TaskManagerV3() {
        taskQueue = new SmartQueue<>();
    }

    public void start() {
    }

    public void stop() {
    }

    protected void insertTask(Task task) {
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

    protected Task get() {
        return taskQueue.pollFirst();
    }

    public void remove(Task task) {
        taskQueue.remove(task);
    }

    private void clearQueue() {
        taskQueue.clear();
    }

    public int getCount() {
        return taskQueue.size();
    }

    public void postTask(Task task) {
        postTask(null, task);
    }

    public void postTask(TaskManagerV3 parent, Task task) {
        this.parent = parent;
        if (exists(task)) {
            return;
        }
        insertTask(task);
        resolveThread();
    }

    public void postTaskInFirst(Task task) {
        if (exists(task)) {
            return;
        }
        taskQueue.insertFirst(task);
        resolveThread();
    }

    public void moveToFirst(Task task) {
        remove(task);
        postTaskInFirst(task);
    }

    private boolean exists(Task task) {
        for (Task tTask : taskQueue) {
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

            Task nextTask = get();
            if (nextTask == null) break;

            try {
                resolveTask(nextTask);
            } catch (InterruptedException e) {
                LogKit.error(toString() + " " + e.toString());
            }

        }
    }
}

