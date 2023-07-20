package com.dahuaboke.handler.net.template;

import okhttp3.Request;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
public class GetTemplate extends AbstractTemplate {

    @Override
    public Request forward(String url, Map<String, String> headers, String body) {
        Request.Builder builder = new Request.Builder();
        return builder.get().url(url).build();
    }
}
