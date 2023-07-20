package com.dahuaboke.handler.net.template;

import okhttp3.Request;
import org.springframework.stereotype.Component;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class GetMethodTemplate extends AbstractMethodTemplate {

    @Override
    public Request forward(Request.Builder builder, String url, String body) {
        return builder.get().url(url).build();
    }
}
