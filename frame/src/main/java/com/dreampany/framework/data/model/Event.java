package com.dreampany.framework.data.model;


import com.dreampany.framework.data.enums.NotifyType;
import com.dreampany.framework.data.enums.Type;

/**
 * Created by nuc on 5/20/2017.
 */

public abstract class Event<T extends Type> extends BaseSerial {

    private enum Result {
        SUCCESS, ERROR
    }

    private T type;
    private Result result;
    private boolean priority;

    @Override
    public boolean equals(Object inObject) {
        if (Event.class.isInstance(inObject)) {
            Event event = (Event) inObject;
            return event.type.equals(type);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public void successResult() {
        result = Result.SUCCESS;
    }

    public void errorResult() {
        result = Result.ERROR;
    }

    public void highPriority() {
        priority = true;
    }

    public void lowPriority() {
        priority = false;
    }

    public boolean isPriority() {
        return priority;
    }

    public void setType(T type) {
        this.type = type;
    }

    public T getType() {
        return type;
    }

    public boolean isAlert() {
        return type == NotifyType.ALERT;
    }

    public boolean isErrorResult() {
        return result == Result.ERROR;
    }
}
