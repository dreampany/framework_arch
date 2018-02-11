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
    private JsonUtil() {
    }

    public static JsonObject getJsonObject(String json) {
        JsonParser parser = new JsonParser();
        return parser.parse(json).getAsJsonObject();
    }

    public static String getString(JsonObject json, String key, String defaultValue) {
        if (json.has(key)) {
            JsonElement value = json.get(key);
            if (!value.isJsonNull()) {
                return value.getAsString();
            }
        }
        return defaultValue;
    }

    public static int getInt(JsonObject json, String key, int defaultValue) {
        if (json.has(key)) {
            JsonElement value = json.get(key);
            if (!value.isJsonNull()) {
                return value.getAsInt();
            }
        }
        return defaultValue;
    }

    public static long getLong(JsonObject json, String key, long defaultValue) {
        if (json.has(key)) {
            JsonElement value = json.get(key);
            if (!value.isJsonNull()) {
                return value.getAsLong();
            }
        }
        return defaultValue;
    }

    public static float getFloat(JsonObject json, String key, float defaultValue) {
        if (json.has(key)) {
            JsonElement value = json.get(key);
            if (!value.isJsonNull()) {
                return value.getAsFloat();
            }
        }
        return defaultValue;
    }

    public static double getDouble(JsonObject json, String key, double defaultValue) {
        if (json.has(key)) {
            JsonElement value = json.get(key);
            if (!value.isJsonNull()) {
                return value.getAsDouble();
            }
        }
        return defaultValue;
    }

    public static JsonObject getObject(JsonObject json, String key, JsonObject defaultValue) {
        if (json.has(key)) {
            JsonElement value = json.get(key);
            if (!value.isJsonNull()) {
                return value.getAsJsonObject();
            }
        }
        return defaultValue;
    }

    public static JsonArray getArray(JsonObject json, String key, JsonArray defaultValue) {
        if (json.has(key)) {
            JsonElement value = json.get(key);
            if (!value.isJsonNull()) {
                return value.getAsJsonArray();
            }
        }
        return defaultValue;
    }

    public static List<String> getStringList(JsonObject json, String key, List<String> defaultValue) {
        JsonArray array = json.has(key) ? json.get(key).getAsJsonArray() : null;
        if (array == null || array.size() <= 0) {
            return defaultValue;
        }
        List<String> items = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            if (!element.isJsonNull()) {
                items.add(element.getAsString());
            }
        }
        return items;
    }


}
