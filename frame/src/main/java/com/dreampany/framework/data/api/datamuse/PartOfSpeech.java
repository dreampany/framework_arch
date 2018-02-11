package com.dreampany.framework.data.api.datamuse;

/**
 * Created by air on 11/21/17.
 */

public enum PartOfSpeech {

    NOUN("n"),
    VERB("v"),
    ADJECTIVE("adj"),
    ADVERB("adv"),
    PRONOUN("pron"),
    PREPOSITION("prep"),
    CONJUNCTION("conj"),
    INTERJECTION("interj");

    private final String value;

    PartOfSpeech(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public String value() {
        return value;
    }

    public static PartOfSpeech createOf(String value) {

        PartOfSpeech[] parts = PartOfSpeech.values();
        for (PartOfSpeech part : parts) {
            if (part.value().equals(value)) {
                return part;
            }
        }

        return null;
    }
}
