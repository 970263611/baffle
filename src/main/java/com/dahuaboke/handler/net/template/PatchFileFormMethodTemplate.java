package com.dahuaboke.handler.net.template;

import io.netty.handler.codec.http.HttpMethod;
import okhttp3.MultipartBody;
import okhttp3.Request;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class PatchFileFormMethodTemplate extends AbstractFileFormMethodTemplate {

    public PatchFileFormMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    @Override
    Request forward(Request.Builder builder, String url, Map<String, String> headers, String body) {
        MultipartBody.Builder multipartBody = super.buildMultipartBody(headers, body);
        return builder.patch(multipartBody.build()).url(url).build();
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.PATCH;
    }

    @Override
    protected String headerValue() {
        return "multipart/form-data";
    }
}
