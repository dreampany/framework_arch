package com.dreampany.framework.data.model;

import java.util.List;

/**
 * Created by nuc on 4/9/2016.
 */
public class FirebaseLock<T, E, A> {
    public List<T> result;
    public E error;
    public A auth;
    public Class<T> tClass;
}
