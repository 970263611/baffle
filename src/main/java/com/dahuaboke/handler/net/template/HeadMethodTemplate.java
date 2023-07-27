package com.dahuaboke.handler.net.template;

import io.netty.handler.codec.http.HttpMethod;
import okhttp3.Request;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/27 9:47
 * 暂不启用
 */
public class HeadMethodTemplate extends AbstractMethodTemplate {

    public HeadMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    @Override
    Request forward(Request.Builder builder, String url, Map<String, String> headers, String body) {
        return builder.head().url(url).build();
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.HEAD;
    }

    @Override
    protected String headerName() {
        return "";
    }

    @Override
    protected String headerValue() {
        return "";
    }
}
