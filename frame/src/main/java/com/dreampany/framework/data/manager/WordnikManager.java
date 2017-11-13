package com.dreampany.framework.data.manager;

import com.wordnik.client.api.WordApi;
import com.wordnik.client.api.WordsApi;
import com.wordnik.client.common.ApiException;
import com.wordnik.client.model.Definition;
import com.wordnik.client.model.Example;
import com.wordnik.client.model.ExampleSearchResults;
import com.wordnik.client.model.Related;
import com.wordnik.client.model.TextPron;
import com.wordnik.client.model.WordObject;
import com.wordnik.client.model.WordOfTheDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuc on 3/5/2017.
 */

public class WordnikManager {
    private WordnikManager() {
    }

    private static final String WORDNIK_API_KEY = "api_key";
    private static final String WORDNIK_API_KEY_VALUE = "a6714f04f26b9f14e29a920702e0f03dde4b84e98f94fe6fe";

    public static final String RELATED_RELATION_SYNONYM = "synonym";
    public static final String RELATED_RELATION_ANTONYM = "antonym";

    private static WordsApi wordsApi;
    private static WordApi wordApi;

    synchronized private static WordsApi getWordsApi() {
        if (wordsApi == null) {
            wordsApi = new WordsApi();
            wordsApi.addHeader(WORDNIK_API_KEY, WORDNIK_API_KEY_VALUE);
        }
        return wordsApi;
    }

    synchronized private static WordApi getWordApi() {
        if (wordApi == null) {
            wordApi = new WordApi();
            wordApi.addHeader(WORDNIK_API_KEY, WORDNIK_API_KEY_VALUE);
        }
        return wordApi;
    }

    synchronized public static WordOfTheDay getWordOfTheDay(String date) {

        try {
            WordOfTheDay wordOfTheDay = getWordsApi().getWordOfTheDay(date);
            return wordOfTheDay;
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    synchronized public static WordObject getWord(String word) {

        try {
            String useCanonical = "true";
            String includeSuggestions = "false";
            WordObject wordObject = getWordApi().getWord(word, useCanonical, includeSuggestions);
            return wordObject;
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    synchronized public static List<String> getRelated(String word, String relationshipTypes, int limit) {
        try {
            String useCanonical = "true";

            List<Related> relateds = getWordApi().getRelatedWords(word, relationshipTypes, useCanonical, limit);
            if (relateds == null || relateds.isEmpty()) return null;

            List<String> relates = null;
            for (Related related : relateds) {
                List<String> words = related.getWords();
                if (words != null && !words.isEmpty()) {
                    if (relates == null) {
                        relates = new ArrayList<>();
                    }
                    relates.addAll(words);
                }
            }
            return relates;

        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    synchronized public static List<TextPron> getPronunciations(String word, int limit) {
        try {
            String sourceDictionary = null;
            String typeFormat = null;
            String useCanonical = "true";

            List<TextPron> pronunciations = getWordApi().getTextPronunciations(word, sourceDictionary, typeFormat, useCanonical, limit);
            return pronunciations;
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    synchronized  public static List<Definition> getDefinitions(String word, int limit) {
        try {
            String partOfSpeech = null;
            String sourceDictionaries = null;
            String includeRelated = null;
            String useCanonical = "true";
            List<Definition> definitions = getWordApi().getDefinitions(word, partOfSpeech, sourceDictionaries, limit, includeRelated, useCanonical, null);
            return definitions;
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    synchronized public static List<Definition> getDefinitions(String word, String partOfSpeech, int limit) {
        try {
            String sourceDictionaries = null;
            String includeRelated = null;
            String useCanonical = "true";
            List<Definition> definitions = getWordApi().getDefinitions(word, partOfSpeech, sourceDictionaries, limit, includeRelated, useCanonical, null);
            return definitions;
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    synchronized  public static List<Example> getExamples(String word, int limit) {
        try {
            String includeDuplicates = "true";
            String useCanonical = "true";
            int skip = 0;
            ExampleSearchResults results = getWordApi().getExamples(word, includeDuplicates, useCanonical, skip, limit);
            if (results != null) {
                return results.getExamples();
            }
        } catch (IllegalArgumentException | ApiException | NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }



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
        resolveRelated(word, RELATED_RELATION_SYNONYM);
        resolveRelated(word, RELATED_RELATION_ANTONYM);
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


    public static List<Word> toQuery(String query, long limit) {

        return null;
    }*/
}
