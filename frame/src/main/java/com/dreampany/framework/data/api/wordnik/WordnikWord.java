package com.dreampany.framework.data.api.wordnik;

import java.util.List;

/**
 * Created by air on 11/22/17.
 */

public class WordnikWord {

    private String word;
    private String partOfSpeech;
    private String pronunciation;
    private List<WordnikDefinition> definitions;
    private List<String> examples;
    private List<String> synonyms;
    private List<String> antonyms;

    WordnikWord(String word) {
        this.word = word;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public void setDefinitions(List<WordnikDefinition> definitions) {
        this.definitions = definitions;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public void setAntonyms(List<String> antonyms) {
        this.antonyms = antonyms;
    }

    public String getWord() {
        return word;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public List<WordnikDefinition> getDefinitions() {
        return definitions;
    }

    public List<String> getExamples() {
        return examples;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }
}
