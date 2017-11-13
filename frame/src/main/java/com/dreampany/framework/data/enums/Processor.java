package com.dreampany.framework.data.enums;

import android.os.Parcelable;

import com.dreampany.framework.data.model.Task;

public interface Processor extends Parcelable {
    void process(Task task);
}