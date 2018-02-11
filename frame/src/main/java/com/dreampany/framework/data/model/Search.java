package com.dreampany.framework.data.model;

/**
 * Created by air on 11/28/17.
 */

public class Search extends BaseSerial {
    private String text;

    public Search(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}

