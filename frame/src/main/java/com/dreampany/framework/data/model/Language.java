package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by air on 11/4/17.
 */

@Entity(indices = {@Index(value = "code", unique = true)})
public class Language extends BaseSerial implements Comparable<Language> {

    @PrimaryKey
    @NonNull
    private String code;
    private String name;

    public Language(@NonNull String code) {
        this(code, null);
    }

    @Ignore
    public Language(@NonNull String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public boolean equals(Object inObject) {
        if (Language.class.isInstance(inObject)) {
            Language language = (Language) inObject;
            return this.code.equals(language.code);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public int compareTo(@NonNull Language language) {
        return code.compareToIgnoreCase(language.code);
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
