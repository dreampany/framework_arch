package com.dreampany.framework.data.http;

import com.dreampany.framework.data.util.DataUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by nuc on 6/11/2016.
 */
public final class HttpManager {

    private static final String EQUAL = "=";
    private static final String AND = "&";
    private static final String APPEND = "?";

    private String failedURLS = "";
    private String succeededURLS = "";
    private String incorrectURLS = "";

    private OkHttpClient client;

    public HttpManager() {
        client = new OkHttpClient();
    }

    private boolean verifyUrl(String url) {
        String urlRegex = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher m = pattern.matcher(url);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public String validateUrl(List<String> urls) throws Exception {

        for (String url : urls) {
            if (verifyUrl(url)) {

                try {
                    URL toUrl = new URL(url);
                    HttpURLConnection http = (HttpURLConnection) toUrl.openConnection();
                    http.setConnectTimeout(5000);
                    if (http.getResponseCode() == URLStatus.HTTP_OK.getStatusCode()) {
                        succeededURLS = succeededURLS + url + "\n";
                    } else {
                        failedURLS = failedURLS + url + "\n";
                    }

                    System.out.println(url + " " + URLStatus.getStatusMessageForStatusCode(http.getResponseCode()));

                } catch (Exception e) {
                    //System.out.print("For url- " + url + "" + e.getMessage());
                    System.out.println(url + " " + e);
                }

            } else {
                incorrectURLS += "\n" + url;
            }
        }

        return succeededURLS;
    }

    public boolean validUrl(String url) {

        if (verifyUrl(url)) {

            try {
                URL toUrl = new URL(url);
                HttpURLConnection http = (HttpURLConnection) toUrl.openConnection();
                http.connect();
                //  http.setConnectTimeout(3000);
                //     http.setReadTimeout(1000);

                if (http.getResponseCode() == URLStatus.HTTP_OK.getStatusCode()) {
                    return true;
                }

                //System.out.println(url + " " + URLStatus.getStatusMessageForStatusCode(http.getResponseCode()));

            } catch (Exception e) {
                //System.out.print("For url- " + url + "" + e.getMessage());
                //System.out.println(url + " " + e);
            }
        }
        return false;
    }

    public String get(String url) {
        return get(url, null);
    }

    public String get(String host, Map<String, String> params) {
        StringBuilder url = new StringBuilder(host);
        String param = getParam(params);
        if (!DataUtil.isEmpty(param)) {
            url.append(APPEND).append(param);
        }

        return getByUrl(url.toString());
    }

    public String post(String url, Map<String, String> params) {
        FormBody.Builder body = new FormBody.Builder();

        for (Map.Entry<String, String> param : params.entrySet()) {
            body.add(param.getKey(), param.getValue());
        }

        Request request = new Request.Builder()
                .url(url)
                .post(body.build())
                .build();

        return request(request);
    }

    public String post(String url, MediaType mediaType, Map<String, String> params) {
        FormBody.Builder body = new FormBody.Builder();

        for (Map.Entry<String, String> param : params.entrySet()) {
            body.add(param.getKey(), param.getValue());
        }

        Request request = new Request.Builder()
                .url(url)
                .post(body.build())
                .addHeader("Content-Type", mediaType.toString())
                .build();

        return request(request);
    }

    private String getByUrl(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        return request(request);
    }

    private String request(Request request) {
        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                ResponseBody body = response.body();

                if (body != null) {
                    return body.string();
                }
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    private String getParam(Map<String, String> params) {
        if (DataUtil.isEmpty(params)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (builder.length() > 0) {
                builder.append(AND);
            }
            builder.append(entry.getKey()).append(EQUAL).append(entry.getValue());
        }
        return builder.toString();
    }


}
