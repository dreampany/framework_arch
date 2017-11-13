package com.dreampany.framework.data.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nuc on 1/18/2017.
 */

public final class JsonUtil {
    private JsonUtil() {}

    public static JsonObject getJsonObject(String json) {
        JsonParser parser = new JsonParser();
        return parser.parse(json).getAsJsonObject();
    }

    public static String getString(JsonObject json, String key, String defaultValue) {
        return json.has(key) ? json.get(key).getAsString() : defaultValue;
    }

    public static long getLong(JsonObject json, String key, long defaultValue) {
        return json.has(key) ? json.get(key).getAsLong() : defaultValue;
    }

    public static JsonArray getArray(JsonObject json, String key, JsonArray defaultValue) {
        return json.has(key) ? json.get(key).getAsJsonArray() : defaultValue;
    }

    public static List<String> getStringList(JsonObject json, String key, List<String> defaultValue) {
        JsonArray array = json.has(key) ? json.get(key).getAsJsonArray() : null;
        if (array == null || array.size() <= 0) {
            return defaultValue;
        }
        List<String> items = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            items.add(element.getAsString());
        }
        return items;
    }
}
