package ru.netology;

import org.apache.hc.core5.http.NameValuePair;

import java.util.List;
import java.util.Map;

public class Request {

    private String method;
    private String path;
    private Map<String, String> headers;
    private String body;
    private List<NameValuePair> paramPairs;

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

    public List<NameValuePair>  paramPairs() {
        return paramPairs;
    }

    public void setParamPairs(List<NameValuePair>  paramPairs) {
        this.paramPairs = paramPairs;
    }

    private Request() {
    }

    private static class Helper {
        static final Request INSTANCE = new Request();
    }

    public static Request getInstance() {
        return Helper.INSTANCE;
    }

    @Override
    public String toString() {
        return "Request{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                //", headers=" + headers +
                //", body='" + body + '\'' +
                ", paramPairs=" + paramPairs +
                '}';
    }
}
