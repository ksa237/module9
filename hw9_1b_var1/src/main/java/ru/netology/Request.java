package ru.netology;

import java.util.Map;

public class Request {

    private  String method;
    private  String path;
    private  Map<String, String> headers;
    private  String body;

    public String method() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String path() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String body() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private Request() {}

    private static class Helper{
        static final Request INSTANCE = new Request();
    }

    public static Request getInstance() {
        return Helper.INSTANCE;
    }

}
