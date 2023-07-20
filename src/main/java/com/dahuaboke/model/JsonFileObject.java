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
    private Object response;

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

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
