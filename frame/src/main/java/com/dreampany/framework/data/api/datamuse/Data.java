package com.dreampany.framework.data.api.datamuse;

import java.util.List;

/**
 * Created by air on 11/20/17.
 */

public class Data {
    private String word;
    private PartOfSpeech speech;
    private String pronunciation;
    private List<Def> defs;

    public Data(String word, PartOfSpeech speech, String pronunciation, List<Def> defs) {
        this.word = word;
        this.speech = speech;
        this.pronunciation = pronunciation;
        this.defs = defs;
    }

    public String getWord() {
        return word;
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

    public String getPronunciation() {
        return pronunciation;
    }

    public List<Def> getDefs() {
        return defs;
    }
}
