package com.dreampany.framework.data.http;

/**
 * Created by nuc on 6/11/2016.
 */
public enum URLStatus {

    HTTP_OK(200, "OK", "SUCCESS"),
    NO_CONTENT(204, "No Content", "SUCCESS"),
    MOVED_PERMANENTLY(301, "Moved Permanently", "SUCCESS"),
    NOT_MODIFIED(304, "Not modified", "SUCCESS"),
    USE_PROXY(305, "Use Proxy", "SUCCESS"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "ERROR"),
    NOT_FOUND(404, "Not Found", "ERROR");

    private int statusCode;
    private String httpMessage;
    private String result;

    private URLStatus(int statusCode, String httpMessage, String result) {
        this.statusCode = statusCode;
        this.httpMessage = httpMessage;
        this.result = result;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static String getStatusMessageForStatusCode(int statusCode) {
        String returnStatusMessage = "Status Not Defined";
        for (URLStatus object : URLStatus.values()) {
            if (object.statusCode == statusCode) {
                returnStatusMessage = object.httpMessage;
            }
        }
        return returnStatusMessage;
    }

    public static String getResultForStatusCode(int statusCode) {
        String returnResultMessage = "Result Not Defined";
        for (URLStatus object : URLStatus.values()) {
            if (object.statusCode == statusCode) {
                returnResultMessage = object.result;
            }
        }
        return returnResultMessage;
    }
}
