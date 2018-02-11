package com.dreampany.framework.data.api.datamuse;

/**
 * Created by air on 11/21/17.
 */

public class Def {
    private PartOfSpeech speech;
    private String text;

    public Def(PartOfSpeech speech, String text){
        this.speech = speech;
        this.text = text;
    }

    public PartOfSpeech getSpeech() {
        return speech;
    }

    public String getSpeechAsString() {
        if (speech != null) {
            return speech.toString();
        }
        return null;
    }

    public String getText() {
        return text;
    }
}
