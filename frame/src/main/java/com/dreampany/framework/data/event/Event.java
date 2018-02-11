package com.dreampany.framework.data.event;


import com.dreampany.framework.data.enums.Result;
import com.dreampany.framework.data.enums.Type;
import com.dreampany.framework.data.enums.Ui;
import com.dreampany.framework.data.model.BaseSerial;

/**
 * Created by nuc on 5/20/2017.
 */

public abstract class Event<T> extends BaseSerial {

    protected T item;
    protected Type type;
    protected Type subtype;
    protected Type taskType;
    protected Result result;
    protected Type cause;
    protected Ui ui;

    public Event() {
        this(null);
    }

    public Event(T item) {
        this(item, null);
    }

    public Event(T item, Type type) {
        this(item, type, null);
    }

    public Event(T item, Type type, Type subtype) {
        this(item, type, subtype, null);
    }

    public Event(T item, Type type, Type subtype, Type taskType) {
        this(item, type, subtype, taskType, null);
    }

    public Event(T item, Type type, Type subtype, Type taskType, Result result) {
        this(item, type, subtype, taskType, result, null);
    }

    public Event(T item, Type type, Type subtype, Type taskType, Result result, Ui ui) {
        this.item = item;
        this.type = type;
        this.subtype = subtype;
        this.taskType = taskType;
        this.result = result;
        this.ui = ui;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setSubtype(Type subtype) {
        this.subtype = subtype;
    }

    public void setTaskType(Type taskType) {
        this.taskType = taskType;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void setCause(Type cause) {
        this.cause = cause;
    }

    public void setUi(Ui ui) {
        this.ui = ui;
    }

    public T getItem() {
        return item;
    }

    public Type getType() {
        return type;
    }

    public Type getSubtype() {
        return subtype;
    }

    public Type getTaskType() {
        return taskType;
    }

    public Result getResult() {
        return result;
    }

    public Type getCause() {
        return cause;
    }

    public Ui getUi() {
        return ui;
    }

    public boolean isError() {
        return Result.ERROR.equals(result);
    }
}
