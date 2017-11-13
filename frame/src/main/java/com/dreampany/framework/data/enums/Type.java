package com.dreampany.framework.data.enums;

import android.os.Parcelable;

public interface Type extends Parcelable {
    boolean equals(Type type);

    String value();

    int ordinalValue();
}