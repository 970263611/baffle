package com.dahuaboke.handler.net.template;

import com.dahuaboke.handler.net.HttpClient;
import com.dahuaboke.handler.net.RequestCallBack;
import com.dahuaboke.model.BaffleResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    public void exec(String url, Map<String, String> headers, String body, RequestCallBack requestCallBack) throws JsonProcessingException {
        Request request = forward(url, headers, body);
        httpClient.getInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                requestCallBack.complate(new BaffleResponse(false, e.fillInStackTrace().toString()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                requestCallBack.complate(new BaffleResponse(true, response.body().string()));
            }
        });
    }

    abstract Request forward(String url, Map<String, String> headers, String body) throws JsonProcessingException;
}
