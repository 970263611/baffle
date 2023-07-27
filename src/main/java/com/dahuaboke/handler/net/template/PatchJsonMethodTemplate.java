package com.dahuaboke.handler.net.template;

import io.netty.handler.codec.http.HttpMethod;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/27 9:37
 */
public class PatchJsonMethodTemplate extends AbstractMethodTemplate {

    public PatchJsonMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    @Override
    Request forward(Request.Builder builder, String url, Map<String, String> headers, String body) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(body, JSON);
        return builder.patch(requestBody).url(url).build();
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.PATCH;
    }

    @Override
    protected String headerValue() {
        return "application/json";
    }

    @Override
    protected boolean defaultMethodTemplate() {
        return true;
    }
}
