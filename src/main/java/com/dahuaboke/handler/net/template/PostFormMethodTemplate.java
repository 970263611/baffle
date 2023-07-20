package com.dahuaboke.handler.net.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.FormBody;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class PostFormMethodTemplate extends AbstractMethodTemplate {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    Request forward(Request.Builder builder, String url, String body) throws JsonProcessingException {
        FormBody.Builder builderPostForm = new FormBody.Builder();
        if (body != null) {
            Map<String, String> bodyMap = objectMapper.readValue(body, Map.class);
            bodyMap.forEach((k, v) -> {
                builderPostForm.add(k, v);
            });
        }
        FormBody formBody = builderPostForm.build();
        return builder.post(formBody).url(url).build();
    }
}
