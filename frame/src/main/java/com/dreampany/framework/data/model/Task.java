package com.dreampany.framework.data.model;

import com.dreampany.framework.data.enums.Density;
import com.dreampany.framework.data.enums.Priority;
import com.dreampany.framework.data.enums.Result;
import com.dreampany.framework.data.enums.TaskType;
import com.dreampany.framework.data.enums.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuc on 11/3/2016.
 */

public abstract class Task<T extends BaseSerial, X extends Type, Y extends Type, S extends Type> extends BaseSerial {

    protected T item;
    protected List<T> inputs;
    protected List<T> outputs;
    protected X type;
    protected Y subtype;
    protected S state;
    protected TaskType taskType;
    protected Density density;
    protected Priority priority;
    protected Type status;
    protected Result result;
    protected Type cause;
    protected String id;
    protected String title;
    protected String comment;
    protected boolean progressive;
    protected boolean persistent;
    protected boolean publishable;
    protected boolean singly;
    protected boolean fully;
    private boolean withCache;

    protected Task() {
        singly = true;
        taskType = TaskType.PRODUCE;
        priority = Priority.HIGH;
        result = Result.ERROR;
    }

    @Override
    public boolean equals(Object inObject) {
        if (Task.class.isInstance(inObject)) {
            Task task = (Task) inObject;
            boolean equalItem = item == null || item.equals(task.item);
            boolean equalType = type == null || type.equals(task.type);
            boolean equalSubtype = subtype == null || subtype.equals(task.subtype);
            boolean equalState = state == null || state.equals(task.state);
            boolean equalTaskType = taskType == null || taskType.equals(task.taskType);
            boolean equalCache = withCache == task.withCache;
            return equalItem && equalType && equalSubtype && equalState && equalTaskType && equalCache;
        }
        return false;
    }

/*    protected Task(Parcel in) {
        if (in.readByte() == 0) {
            item = null;
        } else {
            Class<?> itemClazz = (Class<?>) in.readSerializable();
            item = in.readParcelable(itemClazz.getClassLoader());
        }
        //type = in.readParcelable(Type.class.getClassLoader());
        //itemSource = in.readParcelable(Type.class.getClassLoader());
        //taskType = in.readParcelable(Type.class.getClassLoader());
        //density = in.readParcelable(Type.class.getClassLoader());
        //priority = in.readParcelable(Type.class.getClassLoader());
        //status = in.readParcelable(Type.class.getClassLoader());
    }*/

/*    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        if (item == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            Class<?> itemClazz = item.getClass();
            dest.writeSerializable(itemClazz);
            dest.writeParcelable(item, flags);
        }
        dest.writeParcelable(type, flags);
        dest.writeParcelable(itemSource, flags);
        dest.writeParcelable(taskType, flags);
        dest.writeParcelable(density, flags);
        dest.writeParcelable(priority, flags);
        dest.writeParcelable(status, flags);
    }*/

    public Task<T, X, Y, S> setItem(T item) {
        this.item = item;
        return this;
    }

    public Task<T, X, Y, S> setInputs(List<T> inputs) {
        this.inputs = inputs;
        singly = false;
        return this;
    }

    public Task<T, X, Y, S> setOutputs(List<T> outputs) {
        this.outputs = outputs;
        return this;
    }

    public Task<T, X, Y, S> setType(X type) {
        this.type = type;
        return this;
    }

    public Task<T, X, Y, S> setSubtype(Y subtype) {
        this.subtype = subtype;
        return this;
    }

    public Task<T, X, Y, S> setState(S state) {
        this.state = state;
        return this;
    }

    public Task<T, X, Y, S> setTaskType(TaskType taskType) {
        this.taskType = taskType;
        return this;
    }

    public Task<T, X, Y, S> setDensity(Density density) {
        this.density = density;
        return this;
    }

    public Task<T, X, Y, S> setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public Task<T, X, Y, S> setStatus(Type status) {
        this.status = status;
        return this;
    }

    public Task<T, X, Y, S> setResult(Result result) {
        this.result = result;
        return this;
    }

    public Task<T, X, Y, S> setCause(Type cause) {
        this.cause = cause;
        return this;
    }

    public Task<T, X, Y, S> setId(String id) {
        this.id = id;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Task<T, X, Y, S> setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Task<T, X, Y, S> setProgressive(boolean progressive) {
        this.progressive = progressive;
        return this;
    }

    public Task<T, X, Y, S> setPersistent(boolean persistent) {
        this.persistent = persistent;
        return this;
    }

    public Task<T, X, Y, S> setPublishable(boolean publishable) {
        this.publishable = publishable;
        return this;
    }

    public Task<T, X, Y, S> setSingly(boolean singly) {
        this.singly = singly;
        return this;
    }

    public Task<T, X, Y, S> setFully(boolean fully) {
        this.fully = fully;
        return this;
    }

    public void setWithCache(boolean withCache) {
        this.withCache = withCache;
    }

    public T getItem() {
        return item;
    }

    public List<T> getInputs() {
        return inputs;
    }

    public List<T> getOutputs() {
        return outputs;
    }

    public X getType() {
        return type;
    }

    public Y getSubtype() {
        return subtype;
    }

    public S getState() {
        return state;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public Density getDensity() {
        return density;
    }

    public Priority getPriority() {
        return priority;
    }

    public Type getStatus() {
        return status;
    }

    public Result getResult() {
        return result;
    }

    public Type getCause() {
        return cause;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getComment() {
        return comment;
    }

    public boolean isProgressive() {
        return progressive;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public boolean isPublishable() {
        return publishable;
    }

    public boolean isSingly() {
        return singly;
    }

    public boolean isFully() {
        return fully;
    }

    public boolean isWithCache() {
        return withCache;
    }

    //special api
    public void copyFrom(Task<T, X, Y, S> task) {
        setPriority(task.getPriority());
        setPersistent(task.isPersistent());
        setPublishable(task.isPublishable());
    }

    public void addOutput(T output) {
        if (outputs == null) {
            outputs = new ArrayList<>();
        }
        if (!outputs.contains(output)) {
            outputs.add(output);
        }
    }

    public boolean hasInputs() {
        return inputs != null && !inputs.isEmpty();
    }

    public boolean hasOutputs() {
        return outputs != null && !outputs.isEmpty();
    }

    public void goNext() {
        int ordinal = taskType.ordinal();
        taskType = TaskType.valueOf(ordinal + 1);
    }

    public boolean isHighPriority() {
        return priority == Priority.OMG || priority == Priority.HIGH;
    }

    public boolean isError() {
        return Result.ERROR.equals(result);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String itemString = item != null ? item.toString() : null;
        builder.append("Item: " + itemString);
        builder.append(" Type: " + type);
        builder.append(" Subtype: " + subtype);
        builder.append(" TaskType: " + taskType);
        builder.append(" Priority: " + priority);
        return builder.toString();
    }

    protected void copy(Task<T, X, Y, S> task) {
        item = task.item;
        inputs = task.inputs;
        outputs = task.outputs;
        type = task.type;
        subtype = task.subtype;
        state = task.state;
        taskType = task.taskType;
        density = task.density;
        priority = task.priority;
        status = task.status;
        result = task.result;
        cause = task.cause;
        id = task.id;
        title = task.title;
        comment = task.comment;
        progressive = task.progressive;
        persistent = task.persistent;
        persistent = task.persistent;
        publishable = task.publishable;
        singly = task.singly;
        fully = task.fully;
        withCache = task.withCache;
    }
}
