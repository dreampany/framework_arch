package com.dreampany.framework.data.structure;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by nuc on 12/24/2016.
 */

public class SmartQueue<T> extends LinkedBlockingDeque<T> {

    public boolean insertFirst(T t) {
        if (t == null) {
            return false;
        }
        while (true) {
            try {
                putFirst(t);
                return false;
            } catch (InterruptedException e) {
            }
        }
    }

    public boolean insertFirstUniquely(T t) {
        if (t == null) {
            return false;
        }
        if (contains(t)) {
            remove(t);
        }
        while (true) {
            try {
                putFirst(t);
                return false;
            } catch (InterruptedException e) {
            }
        }
    }

    public boolean insertLast(T t) {
        if (t == null) {
            return false;
        }
        while (true) {
            try {
                super.putLast(t);
                return false;
            } catch (InterruptedException e) {
            }
        }
    }

    public boolean insertLastUniquely(T t) {
        if (t == null) {
            return false;
        }
        if (contains(t)) {
            remove(t);
        }
        while (true) {
            try {
                super.putLast(t);
                return false;
            } catch (InterruptedException e) {
            }
        }
    }

    public T pollFirst() {
        return super.pollFirst();
    }

    public T peekFirst() {
        return super.peekFirst();
    }

    public T pollFirst(long timeout) {
        try {
            return super.pollFirst(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return null;
        }
    }

    public T takeFirst() {
        try {
            return super.takeFirst();
        } catch (InterruptedException e) {
            return null;
        }
    }

    public void removeAll() {
        super.clear();
    }
}

