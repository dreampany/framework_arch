package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.enums.Priority;
import com.dreampany.framework.data.model.Task;
import com.dreampany.framework.data.structure.SmartQueue;
import com.dreampany.framework.data.thread.Runner;

/**
 * Created by nuc on 5/20/2017.
 */

public abstract class Manager<T extends Task> extends Runner {

    private final SmartQueue<T> tasks, backTasks;
    //private BackRunner backRunner;

    protected Manager() {
        tasks = new SmartQueue<>();
        backTasks = new SmartQueue<>();
    }

    @Override
    public void start() {
        super.start();
/*        if (backRunner == null || !backRunner.isRunning()) {
            backRunner = new BackRunner();
            backRunner.start();
        }*/
    }

    @Override
    public void stop() {
        super.stop();
 /*       if (backRunner != null && backRunner.isRunning()) {
            backRunner.stop();
        }*/
    }

    @Override
    public void run() {
        super.run();
    }

    public void clearAll() {
        tasks.clear();
//        backTasks.clear();
        interrupt();
        //      if (backRunner != null) {
        //        backRunner.interrupt();
        //    }
    }

    public void putTask(T task) {
        tasks.insertLast(task);
        start();
    }

    public void putTaskUniquely(T task) {
        tasks.insertLastUniquely(task);
        start();
    }

    public void putTaskPriority(T task) {
        Priority priority = task.getPriority();
        if (priority == Priority.HIGH || priority == Priority.OMG) {
            tasks.insertFirst(task);
        } else {
            tasks.insertLast(task);
        }
        start();
    }

/*    public void putBackTask(T task) {
        backTasks.insertLast(task);
        //Manager.this.start();
    }

    public void putBackTaskUniquely(T task) {
        backTasks.insertLastUniquely(task);
        //Manager.this.start();
    }*/

/*    public void putBackTaskPriority(T task) {
        Priority priority = task.getPriority();
        if (priority == Priority.HIGH || priority == Priority.OMG) {
            backTasks.insertFirst(task);
        } else {
            backTasks.insertLast(task);
        }
        //Manager.this.start();
    }*/

    public T takeTask() {
        return tasks.takeFirst();
    }

/*    public T takeBackTask() {
        return backTasks.takeFirst();
    }*/

    public T pollTask() {
        return tasks.pollFirst();
    }

/*    public T pollBackTask() {
        return backTasks.pollFirst();
    }*/

    public int count() {
        return tasks.size();
    }

/*    public int backCount() {
        return backTasks.size();
    }

    protected boolean backLooping() throws InterruptedException {
        return false;
    }

    private class BackRunner extends Runner {

        BackRunner() {

        }

        @Override
        protected boolean looping() throws InterruptedException {
            return backLooping();
        }
    }*/
}
