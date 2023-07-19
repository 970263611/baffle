package com.dahuaboke.handler.service;

import com.dahuaboke.handler.net.HttpClient;
import com.dahuaboke.spring.SpringProperties;
import io.netty.handler.codec.http.HttpMethod;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dahua
 * @time 2023/7/17 10:46
 */
@Component
public class ForwardService {

    @Autowired
    private SpringProperties springProperties;
    @Autowired
    private HttpClient httpClient;

    public String forward(HttpMethod httpType, String uri, Map<String, String> headers, String body) {
        return forward(httpType, uri, headers, body, new AtomicInteger(0));
    }

    public String forward(HttpMethod httpType, String uri, Map<String, String> headers, String body, AtomicInteger index) {
        CompletableFuture<String> completableFuture = new CompletableFuture();
        Request request = null;
        Request.Builder builder = new Request.Builder();
        String host = springProperties.getForwardAddress()[index.intValue()];
        if (!host.startsWith("http://") && !host.startsWith("https://")) {
            host = "http://" + host;
        }
        if (host.endsWith("/")) {
            host = host.substring(0, host.length() - 1);
        }
        if (headers != null) {
            headers.forEach((k, v) -> {
                builder.addHeader(k, v);
            });
        }
        switch (httpType.name()) {
            case "GET":
                String url = host + uri;
                System.out.println("发起请求：" + url);
                request = builder.get().url(url).build();
                break;
            case "POST":
                String postUrl = host + uri;
                System.out.println("发起请求：" + postUrl);
                MediaType JSON = MediaType.get("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(body, JSON);
                request = builder.post(requestBody).url(postUrl).build();
                break;
        }
        httpClient.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (index.intValue() < springProperties.getForwardAddress().length - 1) {
                    String forward = forward(httpType, uri, headers, body, new AtomicInteger(index.getAndIncrement()));
                    completableFuture.complete(forward);
                } else {
                    completableFuture.complete(null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                completableFuture.complete(response.body().string());
            }
        });
        try {
            String result = completableFuture.get();
            System.out.println("调用返回结果 -> " + result);
            return result;
        } catch (Exception e) {
            System.out.println("调用失败");
            return null;
        }
    }
}
