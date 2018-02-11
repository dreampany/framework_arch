package com.dreampany.framework.data.model;

import com.dreampany.framework.data.util.DataUtil;

import java.util.List;

/**
 * Created by air on 11/17/17.
 */

public class Text extends BaseSerial {
    private String text;

    public Text(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public List<String> getItems() {
        return DataUtil.getWords(text);
    }
}
