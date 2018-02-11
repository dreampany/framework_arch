package com.dreampany.framework.data.thread;

import com.dreampany.framework.data.util.LogKit;

public abstract class Runner implements Runnable {

    protected long defaultWait = 1000L;
    protected long semiWait = defaultWait / 2;
    protected long quarterWait = semiWait / 2;
    protected long mediumWait = 3 * defaultWait;
    protected long pureWait = 5 * defaultWait;
    protected long superWait = 8 * defaultWait;
    protected long periodWait = 1000L / 100;
    protected long semiPeriodWait = 1000L / 10;
    protected long wait = defaultWait;

    private volatile Thread thread;
    private volatile boolean running;
    private final Object guard = new Object();
    private volatile boolean guarded;

    public void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        thread.interrupt();
        notifyRunner();
    }

    public void interrupt() {
        if (thread == null || thread.isInterrupted() || (!thread.isAlive() && !thread.isDaemon())) {
            return;
        }
        thread.interrupt();
    }

    public void waitRunner(long timeoutMs) {
        if (guarded) {
            //return;
        }
        guarded = true;
        synchronized (guard) {
            try {
                if (timeoutMs > 0L) {
                    guard.wait(timeoutMs);
                } else {
                    guard.wait();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public void notifyRunner() {
        if (!guarded) {
            //return;
        }
        guarded = false;
        synchronized (guard) {
            guard.notify();
        }
    }

    protected void silentStop() {
        if (running) {
            running = false;
        }
    }

    public boolean isExpired(long time, long delay) {
        return System.currentTimeMillis() - time >= delay;
    }

    public boolean isRunning() {
        return running;
    }

    private void waitFor(long timeout) throws InterruptedException {
        Thread.sleep(timeout);
    }

    protected boolean looping() throws InterruptedException {
        return false;
    }

    @Override
    public void run() {
        try {
            while (running) {
                boolean looped = looping();
                if (!looped) {
                    break;
                }
            }
        } catch (InterruptedException interrupt) {
            LogKit.verbose("Interrupted Exception " + interrupt.toString());
        }
        running = false;
    }
}