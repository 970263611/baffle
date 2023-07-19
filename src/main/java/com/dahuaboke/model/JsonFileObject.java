package com.dahuaboke.model;

import io.netty.handler.codec.http.HttpMethod;

/**
 * @author dahua
 * @time 2023/7/17 11:11
 */
public class JsonFileObject {

    private String uri;
    private HttpMethod type;
    private BaffleMode mode;
    private Object result;

    public JsonFileObject() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public HttpMethod getType() {
        return type;
    }

    public void setType(HttpMethod type) {
        this.type = type;
    }

    public BaffleMode getMode() {
        return mode;
    }

    public void setMode(BaffleMode mode) {
        this.mode = mode;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
