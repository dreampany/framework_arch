package com.dreampany.framework.data.model;

import com.dreampany.framework.data.enums.Type;

/**
 * Created by air on 11/14/17.
 */

public class UiTask<T extends BaseSerial, X extends Type, Y extends Type, S extends Type> extends Task<T, X, Y, S> {

    private boolean fullscreen;
    private String keyword;

    public UiTask() {

    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public String getKeyword() {
        return keyword;
    }

    /*    private UiTask(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public static final Creator<UiTask> CREATOR = new Creator<UiTask>() {
        @Override
        public UiTask createFromParcel(Parcel in) {
            return new UiTask(in);
        }

        @Override
        public UiTask[] newArray(int size) {
            return new UiTask[size];
        }
    };*/
}
