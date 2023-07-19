package com.dahuaboke.handler.service;

import com.dahuaboke.spring.SpringProperties;
import io.netty.handler.codec.http.HttpMethod;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author dahua
 * @time 2023/7/17 10:46
 */
@Component
public class ForwardService {

    @Autowired
    private SpringProperties springProperties;
    private OkHttpClient httpClient;

    public ForwardService() {
        httpClient = new OkHttpClient();
    }

    public String forward(HttpMethod httpType, String uri, Map<String, String> headers, String requestContent, Map<String, String> body) {
        return forward(httpType, uri, headers, requestContent, body, new AtomicInteger(0));
    }

    public String forward(HttpMethod httpType, String uri, Map<String, String> headers, String requestContent, Map<String, String> body, AtomicInteger index) {
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
        System.out.println("发起请求：" + host);
        if (headers != null) {
            headers.forEach((k, v) -> {
                builder.addHeader(k, v);
            });
        }
        switch (httpType.name()) {
            case "GET":
                AtomicReference<String> getUrl = new AtomicReference<>(host + uri);
                if (body != null) {
                    getUrl.set(getUrl.get() + "?");
                    body.forEach((k, v) -> {
                        getUrl.set(getUrl.get() + k + "=" + v + "&");
                    });
                }
                if (requestContent != null && !"".equals(requestContent)) {
                    getUrl.set(getUrl.get() + "?" + requestContent);
                }
                request = builder.get().url(getUrl.get()).build();
                break;
            case "POST":
                FormBody.Builder builderPost = new FormBody.Builder();
                if (body != null) {
                    body.forEach((k, v) -> {
                        builderPost.add(k, v);
                    });
                }
                FormBody formBody = builderPost.build();
                String postUrl = host + uri + "?" + requestContent;
                request = builder.post(formBody).url(postUrl).build();
                break;
        }
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (index.intValue() < springProperties.getForwardAddress().length - 1) {
                    String forward = forward(httpType, uri, headers, requestContent, body, new AtomicInteger(index.getAndIncrement()));
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
