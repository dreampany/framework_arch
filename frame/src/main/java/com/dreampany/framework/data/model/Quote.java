package com.dreampany.framework.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Created by air on 11/17/17.
 */

@Entity(indices = {
        @Index(value = {"text", "author", "language"}, unique = true)},
        primaryKeys = {"text", "author", "language"}
)
public class Quote extends Base {

    @NonNull
    private String text;
    @NonNull
    private String author;
    @NonNull
    private String language;

    @Ignore
    public Quote() {

    }

    @Ignore
    public Quote(String text, String author) {
        this(text, author, Locale.ENGLISH.getLanguage());
    }

    public Quote(@NotNull String text, @NotNull String author, @NotNull String language) {
        this.text = text;
        this.author = author;
        this.language = language;
    }

    @Ignore
    private Quote(Parcel in) {
        super(in);
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof Quote) {
            Quote item = (Quote) inObject;
            return text.equals(item.text) && author.equals(item.author) && language.equals(item.language);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text, author, language);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };


    public void setText(@NotNull String text) {
        this.text = text;
    }

    public void setAuthor(@NotNull String author) {
        this.author = author;
    }

    public void setLanguage(@NotNull String language) {
        this.language = language;
    }

    public String getId() {
        return String.valueOf(hashCode());
    }

    @NotNull
    public String getText() {
        return text;
    }

    @NotNull
    public String getAuthor() {
        return author;
    }

    @NotNull
    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return author;
    }
}
