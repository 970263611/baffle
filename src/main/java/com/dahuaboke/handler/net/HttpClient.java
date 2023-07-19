package com.dahuaboke.handler.net;

import com.dahuaboke.spring.SpringProperties;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class HttpClient {

    private OkHttpClient httpClient;

    public HttpClient(SpringProperties springProperties) {
        httpClient = new OkHttpClient
                .Builder()
                .connectTimeout(springProperties.getForwardConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(springProperties.getForwardReadTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }

    public OkHttpClient getInstance() {
        return httpClient;
    }
}
