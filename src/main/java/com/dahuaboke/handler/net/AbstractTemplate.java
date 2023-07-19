package com.dahuaboke.handler.net;

import io.netty.handler.codec.http.HttpMethod;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dahua
 * @time 2023/7/19 22:22
 */
@Component
public abstract class AbstractTemplate {

    @Autowired
    private HttpClient httpClient;

    public String exec(HttpMethod httpType, String uri, Map<String, String> headers, String body){
        Request request = forward(httpType, uri, headers, body);
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

    abstract Request forward(HttpMethod httpType, String uri, Map<String, String> headers, String body);
}
