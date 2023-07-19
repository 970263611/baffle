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

/**
 * @author dahua
 * @time 2023/7/19 22:22
 */
@Component
public abstract class AbstractTemplate {

    @Autowired
    private HttpClient httpClient;

    public String exec(HttpMethod httpType, String uri, Map<String, String> headers, String body, RequestCallBack requestCallBack) {
        Request request = forward(httpType, uri, headers, body);
        httpClient.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestCallBack.complate(false, e.fillInStackTrace().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                requestCallBack.complate(true, response.body().string());
            }
        });
    }

    abstract Request forward(HttpMethod httpType, String uri, Map<String, String> headers, String body);
}
