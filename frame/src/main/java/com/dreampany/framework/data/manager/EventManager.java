package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.model.Event;
import com.dreampany.framework.data.structure.SmartQueue;

import org.greenrobot.eventbus.EventBus;

public abstract class EventManager extends Manager {

    protected SmartQueue<Event> queue;

    protected EventManager() {
        queue = new SmartQueue<>();
    }

    public void putEvent(Event event) {
        if (event.isPriority()) {
            queue.insertFirstUniquely(event);
        } else {
            queue.insertLastUniquely(event);
        }
        start();
    }

    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void unregister(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }

    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }
}
