package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

/**
 * Created by air on 11/2/17.
 */

@Entity(indices = {@Index(value = {"source", "target", "sourceText"}, unique = true)}, primaryKeys = {"source", "target", "sourceText"})
public class Translate extends BaseSerial {

    @NonNull
    private String source;
    @NonNull
    private String target;
    @NonNull
    private String sourceText;
    private String targetText;
    private long time;

    public Translate() {

    }

    @Override
    public boolean equals(Object inObject) {
        if (Translate.class.isInstance(inObject)) {
            Translate translate = (Translate) inObject;
            return source.equals(translate.source) && target.equals(translate.target) && sourceText.equals(translate.sourceText);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return source.hashCode() ^ target.hashCode() ^ sourceText.hashCode();
    }

    public void setSource(@NonNull String source) {
        this.source = source;
    }

    public void setTarget(@NonNull String target) {
        this.target = target;
    }

    public void setSourceText(@NonNull String sourceText) {
        this.sourceText = sourceText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @NonNull
    public String getSource() {
        return source;
    }

    @NonNull
    public String getTarget() {
        return target;
    }

    @NonNull
    public String getSourceText() {
        return sourceText;
    }

    public String getTargetText() {
        return targetText;
    }

    public long getTime() {
        return time;
    }
}
