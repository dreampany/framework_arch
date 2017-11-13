package com.dreampany.framework.data.http;

import com.dreampany.framework.data.util.DataUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by nuc on 6/11/2016.
 */
public final class HttpClient {

    private String failedURLS = "";
    private String succeededURLS = "";
    private String incorrectURLS = "";

    private OkHttpClient client;

    public HttpClient() {
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

    public String get(String host, Map<String, String> params) {
        StringBuilder url = new StringBuilder(host);
        String param = getParam(params);
        if (!DataUtil.isEmpty(param)) {
            url.append("?").append(param);
        }

        return get(url.toString());
    }

    private String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
           Response response = client.newCall(request).execute();
            ResponseBody body = response.body();

            if (body != null) {
                return body.string();
            }

        } catch (IOException ignored) {
        }
        return null;
    }

    private String getParam(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return builder.toString();
    }
}
