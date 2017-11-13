package com.dreampany.framework.data.manager;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Cloud {

    private static final String GOOGLE_TRANSLATE = "https://translate.google.com/#";

    private OkHttpClient client;


    public Cloud() {
        client = new OkHttpClient();
    }

   /* public String translate(String source, String target, String text) {
*//*        String url = GOOGLE_TRANSLATE + source + "/" + target + "/" + text;
        try {
            return get(url);
        } catch (IOException e) {
            return null;
        }*//*
        Translator translate = Translator.getInstance();
        String translated = translate.translate(text, Language.ENGLISH, Language.BENGALI);
        return translated;
    }
*/

    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}
