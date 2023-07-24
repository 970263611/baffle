package com.dahuaboke.handler.net.template;

import com.dahuaboke.model.HttpTemplateMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpMethod;
import okhttp3.FormBody;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class PostFormMethodTemplate extends AbstractMethodTemplate {

    @Autowired
    private ObjectMapper objectMapper;

    public PostFormMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    @Override
    Request forward(Request.Builder builder, String url, String body) {
        FormBody.Builder builderPostForm = new FormBody.Builder();
        if (body != null && !"".equals(body)) {
            Map<String, String> bodyMap = new HashMap();
            try {
                bodyMap = objectMapper.readValue(body, Map.class);
                bodyMap.forEach((k, v) -> {
                    builderPostForm.add(k, v);
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }
        FormBody formBody = builderPostForm.build();
        return builder.post(formBody).url(url).build();
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected HttpTemplateMode httpTemplateMode() {
        return HttpTemplateMode.POST_FORM;
    }

    @Override
    protected String headerName() {
        return "Content-Type";
    }

    @Override
    protected String headerValue() {
        return "multipart/form-data";
    }
}
