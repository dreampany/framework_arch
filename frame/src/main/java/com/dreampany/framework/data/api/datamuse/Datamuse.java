package com.dreampany.framework.data.api.datamuse;

import com.dreampany.framework.data.http.HttpManager;
import com.dreampany.framework.data.util.DataUtil;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by air on 11/20/17.
 */

public final class Datamuse {

    private static final String WORDS_HOST = "http://api.datamuse.com/words";

    private static final String KEY_SP = "sp";
    private static final String KEY_QE = "qe";
    private static final String KEY_MD = "md";
    private static final String KEY_MAX = "max";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_DEFS = "defs";
    private static final String KEY_PRONUNCIATION = "pron:";

    private static final String VALUE_MAX = "1";
    private static final String VALUE_MD = "dpr";

    private static Datamuse instance;

    private HttpManager http;
    private JsonParser parser;

    public Datamuse() {
        http = new HttpManager();
        parser = new JsonParser();
    }

/*    synchronized public static Datamuse onInstance() {
        if (instance == null) {
            instance = new Datamuse();
        }
        return instance;
    }*/

    public Data getData(String word) {
        Map<String, String> params = Maps.newHashMap();
        params.put(KEY_SP, word);
        params.put(KEY_QE, KEY_SP);
        params.put(KEY_MD, VALUE_MD);
        params.put(KEY_MAX, VALUE_MAX);


        String json = http.get(WORDS_HOST, params);

        if (DataUtil.isEmpty(json)) {
            return null;
        }

        try {
            JsonArray array = parser.parse(json).getAsJsonArray();
            if (array == null || array.size() <= 0) {
                return null;
            }

            JsonObject jsonObject = array.get(0).getAsJsonObject();

            if (jsonObject == null) {
                return null;
            }

            PartOfSpeech partOfSpeech = getPartOfSpeech(jsonObject);
            String pronunciation = getPronunciation(jsonObject);
            List<Def> defs = getDefs(jsonObject);

            if (partOfSpeech == null && DataUtil.isEmpty(pronunciation) && DataUtil.isEmpty(defs)) {
                return null;
            }

            return new Data(word, partOfSpeech, pronunciation, defs);
        } catch (NullPointerException | JsonParseException ignored) {

        }
        return null;
    }

    public boolean hasWord(String word) {
        Map<String, String> params = Maps.newHashMap();
        params.put(KEY_SP, word);
        params.put(KEY_MAX, VALUE_MAX);

        String json = http.get(WORDS_HOST, params);

        if (DataUtil.isEmpty(json)) {
            return false;
        }

        try {
            JsonArray array = parser.parse(json).getAsJsonArray();
            if (array == null || array.size() <= 0) {
                return false;
            }

            JsonObject jsonObject = array.get(0).getAsJsonObject();

            if (jsonObject == null) {
                return false;
            }

           return true;
        } catch (NullPointerException | JsonParseException ignored) {

        }
        return false;
    }


    private PartOfSpeech getPartOfSpeech(JsonObject json) {
        JsonArray tags = json.has(KEY_TAGS) ? json.getAsJsonArray(KEY_TAGS) : null;
        if (tags == null || tags.size() <= 0) {
            return null;
        }

        for (JsonElement element : tags) {
            String item = element.getAsString();
            PartOfSpeech speech = PartOfSpeech.createOf(item);
            if (speech != null) {
                return speech;
            }
        }

        return null;
    }

    private String getPronunciation(JsonObject json) {
        JsonArray tags = json.has(KEY_TAGS) ? json.getAsJsonArray(KEY_TAGS) : null;
        if (tags == null || tags.size() <= 0) {
            return null;
        }

        for (JsonElement element : tags) {
            String item = element.getAsString();
            if (item.indexOf(KEY_PRONUNCIATION) == 0) {
                return item.replaceFirst(KEY_PRONUNCIATION, "");
            }
        }

        return null;
    }

    private List<Def> getDefs(JsonObject json) {
        JsonArray defs = json.has(KEY_DEFS) ? json.getAsJsonArray(KEY_DEFS) : null;
        if (defs == null || defs.size() <= 0) {
            return null;
        }

        List<Def> definitions = new ArrayList<>();

        for (JsonElement element : defs) {
            String item = element.getAsString();
            String[] parts = item.split("\t");
            if (parts.length < 2) {
                continue;
            }
            String left = parts[0];
            String right = parts[1];


            PartOfSpeech speech = PartOfSpeech.createOf(left);
            definitions.add(new Def(speech, right));
        }

        return definitions;
    }
}
