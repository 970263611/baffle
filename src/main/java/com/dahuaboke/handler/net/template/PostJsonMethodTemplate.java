package com.dahuaboke.handler.net.template;

import com.dahuaboke.model.HttpTemplateMode;
import io.netty.handler.codec.http.HttpMethod;
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
public class PostJsonMethodTemplate extends AbstractMethodTemplate {

    public PostJsonMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    @Override
    Request forward(Request.Builder builder, String url, Map<String, String> headers, String body) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(body, JSON);
        return builder.post(requestBody).url(url).build();
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.POST;
    }

    @Override
    protected HttpTemplateMode httpTemplateMode() {
        return HttpTemplateMode.POST_JSON;
    }

    @Override
    protected String headerName() {
        return "Content-Type";
    }

    @Override
    protected String headerValue() {
        return "application/json";
    }
}
