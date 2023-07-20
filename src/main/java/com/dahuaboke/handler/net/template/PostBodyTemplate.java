package com.dahuaboke.handler.net.template;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class PostBodyTemplate extends AbstractTemplate {

    @Override
    Request forward(String url, Map<String, String> headers, String body) {
        Request.Builder builder = new Request.Builder();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(body, JSON);
        return builder.post(requestBody).url(url).build();
    }
}
