package com.dahuaboke.handler.net;

import io.netty.handler.codec.http.HttpMethod;
import okhttp3.Request;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
public class GetTemplate extends AbstractTemplate {

    public Request forward(HttpMethod httpType, String uri, Map<String, String> headers, String body) {
        String url = host + uri;
        System.out.println("发起请求：" + url);
        return builder.get().url(url).build();
    }
}
