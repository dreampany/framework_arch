package com.dreampany.framework.data.api.wordnik;

/**
 * Created by air on 11/22/17.
 */

public class WordnikDefinition {
    private String partOfSpeech;
    private String text;

    public WordnikDefinition(String partOfSpeech, String text) {
        this.partOfSpeech = partOfSpeech;
        this.text = text;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }


    public String getText() {
        return text;
    }
}
