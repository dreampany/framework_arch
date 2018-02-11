package com.dreampany.framework.data.manager;

import android.content.Context;

import com.dreampany.framework.data.util.TextUtil;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by air on 9/12/17.
 */

public class LanguageManager {
    private static LanguageManager instance;
    private final BiMap<String, String> languages;

    private LanguageManager() {
        languages = HashBiMap.create();
    }

    synchronized public static LanguageManager onInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public void storeLanguage(Context context, int codeArray, int nameArray) {
        String[] codes = TextUtil.getStringArray(context, codeArray);
        String[] names = TextUtil.getStringArray(context, nameArray);

        for (int index = 0; index < codes.length; index++) {
            languages.put(codes[index], names[index]);
        }
    }

    public List<String> getNames() {
        return new ArrayList<>(languages.values());
    }

    public String getName(String code) {
        return languages.get(code);
    }

    public String getCode(String name) {
        return languages.inverse().get(name);
    }
}
