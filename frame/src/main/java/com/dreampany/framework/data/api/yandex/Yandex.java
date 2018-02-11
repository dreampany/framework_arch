package com.dreampany.framework.data.api.yandex;

import com.dreampany.framework.data.http.HttpManager;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.JsonUtil;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.Map;

/**
 * Created by air on 11/20/17.
 */

public class Yandex {
    private static final String DICTIONARY_KEY = "dict.1.1.20171120T172932Z.1fe70115ddc90808.185d932e11632463f6068ca95d9d3fbf2cf29f94";


    private static final String DICTIONARY_HOST = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup";


    private static final String KEY = "key";
    private static final String KEY_LANG = "lang";
    private static final String KEY_TEXT = "text";
    private static final String KEY_UI = "ui";
    private static final String KEY_FLAGS = "flags";

    private static final String KEY_DEF = "def";


    private static final String VALUE_LANG = "en-en";
    private static final String FLAGS_FAMILY = "FAMILY";
    private static final String FLAGS_MORPHO = "MORPHO";
    private static final String FLAGS_POS_FILTER = "POS_FILTER";


    private HttpManager http;
    private JsonParser parser;

    public Yandex() {
        http = new HttpManager();
        parser = new JsonParser();
    }

    public Dict getDict(String word) {

        Map<String, String> params = Maps.newHashMap();
        params.put(KEY, DICTIONARY_KEY);
        params.put(KEY_LANG, VALUE_LANG);
        params.put(KEY_TEXT, word);

        String json = http.get(DICTIONARY_HOST, params);

        if (DataUtil.isEmpty(json)) {
            return null;
        }

        try {
            JsonObject jsonObject = parser.parse(json).getAsJsonObject();
/*            String status = JsonUtil.getString(jsonObject, STATUS, null);
            if (!"ok".equals(status)) {
                return null;
            }*/

            JsonArray array = JsonUtil.getArray(jsonObject, KEY_DEF, null);

            if (array == null || array.size() <= 0) {
                return null;
            }

          /*  List<Source> sources = new ArrayList<>();
            for (JsonElement element : array) {
                JsonObject item = element.getAsJsonObject();
                Source source = getSource(item);
                if (source != null) {
                    sources.add(source);
                }
            }
            return sources;*/
        } catch (NullPointerException | JsonParseException ignored) {

        }
        return null;


    }
/*
    private Dict getDict(JsonObject json) {

    }*/
}
