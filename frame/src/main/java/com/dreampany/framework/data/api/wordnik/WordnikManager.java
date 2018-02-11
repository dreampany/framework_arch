package com.dreampany.framework.data.api.wordnik;

import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.LogKit;
import com.wordnik.client.api.WordApi;
import com.wordnik.client.api.WordsApi;
import com.wordnik.client.common.ApiException;
import com.wordnik.client.model.Definition;
import com.wordnik.client.model.Example;
import com.wordnik.client.model.ExampleSearchResults;
import com.wordnik.client.model.Related;
import com.wordnik.client.model.SimpleDefinition;
import com.wordnik.client.model.SimpleExample;
import com.wordnik.client.model.TextPron;
import com.wordnik.client.model.WordObject;
import com.wordnik.client.model.WordOfTheDay;
import com.wordnik.client.model.WordSearchResult;
import com.wordnik.client.model.WordSearchResults;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuc on 3/5/2017.
 */

public final class WordnikManager {

    private static final String WORDNIK_API_KEY = "api_key";
    private static final String WORDNIK_API_KEY_VALUE = "464b0c5a35f469103f3610840dc061f1c768aa1c223ffa447";

    private static final String RELATED_SYNONYM = "synonym";
    private static final String RELATED_ANTONYM = "antonym";

    private static final String RELATED_SYNONYM_ANTONYM = "synonym,antonym";

    //private static WordnikManager instance;
    private WordsApi wordsApi;
    private WordApi wordApi;

    public WordnikManager() {
        wordsApi = new WordsApi();
        wordsApi.addHeader(WORDNIK_API_KEY, WORDNIK_API_KEY_VALUE);

        wordApi = new WordApi();
        wordApi.addHeader(WORDNIK_API_KEY, WORDNIK_API_KEY_VALUE);
    }

    public WordnikWord getWordOfTheDay(String date, int limit) {
        try {
            WordOfTheDay wordOfTheDay = wordsApi.getWordOfTheDay(date);
            return getWord(wordOfTheDay, limit);
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            LogKit.error("error " + e.toString());
            return null;
        }
    }

    public WordnikWord getWord(String word, int limit) {
        try {
            String useCanonical = "true";
            String includeSuggestions = "false";
            WordObject wordObject = wordApi.getWord(word, useCanonical, includeSuggestions);
            return getWord(wordObject, limit);
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            LogKit.error("error " + e.toString());
            return null;
        }
    }


    private WordnikWord getWord(WordOfTheDay from, int limit) {
        WordnikWord word = new WordnikWord(from.getWord());
        word.setPartOfSpeech(getPartOfSpeech(from));
        word.setPronunciation(getPronunciation(from));
        word.setDefinitions(getDefinitions(from));
        word.setExamples(getExamples(from));

        List<Related> relateds = getRelateds(from.getWord(), RELATED_SYNONYM_ANTONYM, limit);
        word.setSynonyms(getSynonyms(relateds));
        word.setAntonyms(getAntonyms(relateds));
        return word;
    }

    private WordnikWord getWord(WordObject from, int limit) {
        WordnikWord word = new WordnikWord(from.getWord());

        List<Definition> definitions = getDefinitions(from.getWord(), limit);
        word.setPartOfSpeech(getPartOfSpeech(definitions));
        word.setPronunciation(getPronunciation(from));
        word.setDefinitions(getDefinitions(definitions));

        List<Example> examples = getExamples(from.getWord(), limit);
        word.setExamples(getExamples(examples));

        List<Related> relateds = getRelateds(from.getWord(), RELATED_SYNONYM_ANTONYM, limit);
        word.setSynonyms(getSynonyms(relateds));
        word.setAntonyms(getAntonyms(relateds));

        return word;
    }

    private String getPartOfSpeech(WordOfTheDay word) {
        List<SimpleDefinition> items = word.getDefinitions();
        if (!DataUtil.isEmpty(items)) {
            for (SimpleDefinition item : items) {
                if (!DataUtil.isEmpty(item.getPartOfSpeech())) {
                    return item.getPartOfSpeech();
                }
            }
        }
        return null;
    }

    private String getPartOfSpeech(List<Definition> items) {
        if (!DataUtil.isEmpty(items)) {
            for (Definition item : items) {
                if (!DataUtil.isEmpty(item.getPartOfSpeech())) {
                    return item.getPartOfSpeech();
                }
            }
        }
        return null;
    }

    private String getPronunciation(WordOfTheDay word) {
        return getPronunciation(word.getWord());
    }

    private String getPronunciation(WordObject word) {
        return getPronunciation(word.getWord());
    }

    private List<WordnikDefinition> getDefinitions(WordOfTheDay word) {
        List<SimpleDefinition> items = word.getDefinitions();
        if (!DataUtil.isEmpty(items)) {
            List<WordnikDefinition> definitions = new ArrayList<>(items.size());
            for (SimpleDefinition item : items) {
                definitions.add(new WordnikDefinition(item.getPartOfSpeech(), item.getText()));
            }
            return definitions;
        }
        return null;
    }

    private List<WordnikDefinition> getDefinitions(List<Definition> items) {
        if (!DataUtil.isEmpty(items)) {
            List<WordnikDefinition> definitions = new ArrayList<>(items.size());
            for (Definition item : items) {
                definitions.add(new WordnikDefinition(item.getPartOfSpeech(), item.getText()));
            }
            return definitions;
        }
        return null;
    }

    private List<String> getExamples(WordOfTheDay word) {
        List<SimpleExample> items = word.getExamples();
        if (!DataUtil.isEmpty(items)) {
            List<String> examples = new ArrayList<>(items.size());
            for (SimpleExample item : items) {
                examples.add(item.getText());
            }
            return examples;
        }
        return null;
    }

    private List<String> getExamples(List<Example> items) {
        if (!DataUtil.isEmpty(items)) {
            List<String> examples = new ArrayList<>(items.size());
            for (Example item : items) {
                examples.add(item.getText());
            }
            return examples;
        }
        return null;
    }

    private List<String> getSynonyms(List<Related> relateds) {
        Related related = getRelated(relateds, RELATED_SYNONYM);
        if (related != null) {
            return related.getWords();
        }
        return null;
    }

    private List<String> getAntonyms(List<Related> relateds) {
        Related related = getRelated(relateds, RELATED_ANTONYM);
        if (related != null) {
            return related.getWords();
        }
        return null;
    }


    private String getPronunciation(String word) {
        try {
            String sourceDictionary = null;
            String typeFormat = null;
            String useCanonical = "true";

            List<TextPron> pronunciations = wordApi.getTextPronunciations(word, sourceDictionary, typeFormat, useCanonical, 3);
            if (!DataUtil.isEmpty(pronunciations)) {
                String pronunciation = pronunciations.get(0).getRaw();
                for (int index = 1; index < pronunciations.size(); index++) {
                    if (pronunciation.length() > pronunciations.get(index).getRaw().length()) {
                        pronunciation = pronunciations.get(index).getRaw();
                    }
                }
                pronunciation = pronunciation.replaceAll("(?s)<i>.*?</i>", "");
                return pronunciation;
            }
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            LogKit.error("error " + e.toString());
        }
        return null;
    }

    private List<Definition> getDefinitions(String word, int limit) {
        try {
            String partOfSpeech = null;
            String sourceDictionaries = null;
            String includeRelated = null;
            String useCanonical = "true";
            List<Definition> definitions = wordApi.getDefinitions(word, partOfSpeech, sourceDictionaries, limit, includeRelated, useCanonical, null);
            return definitions;
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            LogKit.error("error " + e.toString());
        }
        return null;
    }

    public List<Example> getExamples(String word, int limit) {
        try {
            String includeDuplicates = "true";
            String useCanonical = "true";
            int skip = 0;
            ExampleSearchResults results = wordApi.getExamples(word, includeDuplicates, useCanonical, skip, limit);
            if (results != null) {
                return results.getExamples();
            }
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            LogKit.error("error " + e.toString());
        }
        return null;
    }

    public List<WordnikWord> query(String query, int limit) {

        String includePartOfSpeech = null;
        String excludePartOfSpeech = null;
        String caseSensitive = "false";
        int minCorpusCount = 5;
        int maxCorpusCount = -1;
        int minDictionaryCount = 1;
        int maxDictionaryCount = -1;
        int minLength = 1;
        int maxLength = -1;
        int skip = 0;

        try {
            WordSearchResults results = wordsApi.searchWords(query, includePartOfSpeech, excludePartOfSpeech,
                    caseSensitive, minCorpusCount, maxCorpusCount, minDictionaryCount, maxDictionaryCount, minLength, maxLength, skip, limit
            );

            if (results != null) {
                List<WordSearchResult> searches = results.getSearchResults();
                if (!DataUtil.isEmpty(searches)) {
                    List<WordnikWord> words = new ArrayList<>(searches.size());
                    for (WordSearchResult result : searches) {
                        words.add(new WordnikWord(result.getWord().toLowerCase()));
                    }
                    return words;
                }
            }
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            LogKit.error("error " + e.toString());
        }
        return null;
    }

    private List<Related> getRelateds(String word, String relationshipTypes, int limit) {
        try {
            String useCanonical = "true";

            List<Related> relateds = wordApi.getRelatedWords(word, relationshipTypes, useCanonical, limit);
            return relateds;

        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            LogKit.error("error " + e.toString());
        }
        return null;
    }


    private Related getRelated(List<Related> relateds, String relationshipType) {
        if (!DataUtil.isEmpty(relateds)) {
            for (Related related : relateds) {
                if (relationshipType.equals(related.getRelationshipType())) {
                    return related;
                }
            }
        }
        return null;
    }

        /*synchronized public static WordObject getWord(String word) {

        try {
            String useCanonical = "true";
            String includeSuggestions = "false";
            WordObject wordObject = getWordApi().getWord(word, useCanonical, includeSuggestions);
            return wordObject;
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            LogKit.error("error " + e.toString());
            return null;
        }
    }*/

/*    */

/*    synchronized public static List<Definition> getDefinitions(String word, String partOfSpeech, int limit) {
        try {
            String sourceDictionaries = null;
            String includeRelated = null;
            String useCanonical = "true";
            List<Definition> definitions = getWordApi().getDefinitions(word, partOfSpeech, sourceDictionaries, limit, includeRelated, useCanonical, null);
            return definitions;
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            LogKit.error("error " + e.toString());
        }
        return null;
    }*/

/*    */

/*    static Word getRandomWord(String includePartOfSpeech) {

        try {

            String hasDictionaryDef = "true";
            WordObject wordObject = getWordsApi().getRandomWord(includePartOfSpeech, null, hasDictionaryDef, null, null, null, null, null, null);

            String wordValue = wordObject.getWord();
            Word randomWord = new Word(wordValue);
            resolve(randomWord);

            LogUtil.logInfo("Random Word: " + randomWord.toString());

            return randomWord;

        } catch (ApiException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    static List<Word> getRandomWords(String includePartOfSpeech) {

        try {

            String hasDictionaryDef = "true";
            String sortBy = "alpha";
            String sortOrder = "asc";
            List<WordObject> wordObjects = getWordsApi().getRandomWords(includePartOfSpeech, null, sortBy, sortOrder, hasDictionaryDef, null, null, null, null, 3, null, 10);

            List<Word> words = new ArrayList<>(wordObjects.size());

            for (WordObject wordObject : wordObjects) {

                String wordValue = wordObject.getWord();

                if (!TextUtil.isAlpha(wordValue)) continue;

                Word word = new Word(wordValue);
                LogUtil.logInfo(word.toString());

                words.add(word);
            }

            return words;

        } catch (ApiException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    static void resolve(Word word) {
        resolveDefinition(word);
        resolveRelated(word, RELATED_SYNONYM);
        resolveRelated(word, RELATED_ANTONYM);
    }

    static void resolveDefinition(Word word) {
        try {
            String sourceDictionaries = null;
            String includeRelated = null;
            String useCanonical = "true";

            List<Definition> definitions = getWordApi().getDefinitions(word.getValue(), word.getPartOfSpeech(), sourceDictionaries, 1, includeRelated, useCanonical, null);

            if (definitions == null || definitions.isEmpty()) return;

            Definition definition = definitions.get(0);
            word.setPartOfSpeech(definition.getPartOfSpeech());
            word.setDefinition(definition.getText());
        } catch (ApiException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    static void setSynonyms(Word word, List<String> synonymValues) {

        LogUtil.logInfo("Synonyms " + synonymValues.toString());

        for (String wordValue : synonymValues) {

            if (wordValue.length() < 3) continue;
            if (!TextUtil.isAlpha(wordValue)) continue;

            Word synonym = new Word(wordValue);
            resolveDefinition(synonym);
            word.addSynonym(synonym);
        }
    }

    static void setAntonyms(Word word, List<String> antonymsValues) {

        LogUtil.logInfo("Antonyms " + antonymsValues.toString());

        for (String wordValue : antonymsValues) {

            if (wordValue.length() < 3) continue;
            if (!TextUtil.isAlpha(wordValue)) continue;

            Word antonym = new Word(wordValue);
            resolveDefinition(antonym);
            word.addAntonym(antonym);
        }
    }


    */
}
