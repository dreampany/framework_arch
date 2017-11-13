package com.dreampany.framework.data.model;

import android.os.Parcel;

import com.dreampany.framework.data.enums.Priority;
import com.dreampany.framework.data.enums.Type;

/**
 * Created by nuc on 11/3/2016.
 */

public abstract class Task<T extends Base> extends Base {

    protected T item;
    protected Type itemType;
    protected Type itemSource;
    protected Type taskType;
    protected Type density;
    protected Type priority;
    protected Type status;

    protected Task() {
        priority = Priority.LOW;
    }

    protected Task(Parcel in) {
        //todo
    }

    public Task<T> setItem(T item) {
        this.item = item;
        return this;
    }

    public Task<T> setItemType(Type itemType) {
        this.itemType = itemType;
        return this;
    }

    public Task<T> setItemSource(Type itemSource) {
        this.itemSource = itemSource;
        return this;
    }

    public Task<T> setTaskType(Type taskType) {
        this.taskType = taskType;
        return this;
    }

    public Task<T> setDensity(Type density) {
        this.density = density;
        return this;
    }

    public Task<T> setPriority(Type priority) {
        this.priority = priority;
        return this;
    }

    public Task<T> setStatus(Type status) {
        this.status = status;
        return this;
    }

    public T getItem() {
        return item;
    }

    public Type getItemType() {
        return itemType;
    }

    public Type getItemSource() {
        return itemSource;
    }

    public Type getTaskType() {
        return taskType;
    }

    public Type getDensity() {
        return density;
    }

    public Type getPriority() {
        return priority;
    }

    public Type getStatus() {
        return status;
    }


    // special api
    public boolean isHighPriority() {
        return taskType == Priority.HIGH;
    }
}
