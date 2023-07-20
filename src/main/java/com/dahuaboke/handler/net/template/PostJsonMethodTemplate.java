package com.dahuaboke.handler.net.template;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class PostJsonMethodTemplate extends AbstractMethodTemplate {

    @Override
    Request forward(Request.Builder builder, String url, String body) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(body, JSON);
        return builder.post(requestBody).url(url).build();
    }
}
