package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

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

    @Ignore
    public Translate() {

    }

    public Translate(@NotNull String source, @NotNull String target, @NotNull String sourceText, String targetText) {
        this.source = source;
        this.target = target;
        this.sourceText = sourceText;
        this.targetText = targetText;
    }

    @Override
    public boolean equals(Object inObject) {
        if (Translate.class.isInstance(inObject)) {
            Translate translate = (Translate) inObject;
            return source.equals(translate.source) && target.equals(translate.target) && sourceText.equals(translate.sourceText) && targetText.equals(translate.targetText);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(source, target, sourceText, targetText);
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

    public void setTargetText(@NotNull String targetText) {
        this.targetText = targetText;
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

    public void adjust() {
        // small one to left
        if (source.compareTo(target) > 0) {
            String t = source;
            source = target;
            target = t;

            t = sourceText;
            sourceText = targetText;
            targetText = t;
        }
    }
}
