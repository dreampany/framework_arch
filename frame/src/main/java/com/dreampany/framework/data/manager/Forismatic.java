package com.dreampany.framework.data.manager;

import com.dreampany.framework.data.http.HttpManager;
import com.dreampany.framework.data.model.Quote;
import com.dreampany.framework.data.util.DataUtil;
import com.dreampany.framework.data.util.JsonUtil;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.Locale;
import java.util.Map;

class Forismatic {
    private static final String HOST = "http://api.forismatic.com/api/1.0/";

    private static final String KEY_METHOD = "method";
    private static final String KEY_FORMAT = "format";
    private static final String KEY_LANGUAGE = "lang";
    private static final String KEY_KEY = "key";


    private static final String KEY_KEY_VALUE = "457653";
    private static final String KEY_METHOD_VALUE = "getQuote";
    private static final String KEY_FORMAT_VALUE = "json";


    private static final String KEY_QUOTE_TEXT = "quoteText";
    private static final String KEY_AUTHOR = "quoteAuthor";

    HttpManager http;
    JsonParser parser;
    Map<String, String> params;
    String language = Locale.ENGLISH.getLanguage();

    Forismatic() {
        http = new HttpManager();
        parser = new JsonParser();
        params = Maps.newHashMap();
        params.put(KEY_METHOD, KEY_METHOD_VALUE);
        params.put(KEY_FORMAT, KEY_FORMAT_VALUE);
        params.put(KEY_LANGUAGE, language);
    }

    Quote get() {

        String json = http.post(HOST, params);
        if (DataUtil.isEmpty(json)) {
            return null;
        }

        try {
            JsonObject jsonObject = parser.parse(json).getAsJsonObject();

            if (jsonObject == null) {
                return null;
            }
            return getQuote(jsonObject, language);
        } catch (NullPointerException | JsonParseException ignored) {

        }
        return null;
    }

    private Quote getQuote(JsonObject json, String language) {
        String text = JsonUtil.getString(json, KEY_QUOTE_TEXT, null);
        String author = JsonUtil.getString(json, KEY_AUTHOR, null);

        return new Quote(text, author, language);
    }
}