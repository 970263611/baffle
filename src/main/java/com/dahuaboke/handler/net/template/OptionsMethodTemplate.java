package com.dahuaboke.handler.net.template;

import io.netty.handler.codec.http.HttpMethod;
import okhttp3.Request;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/27 9:40
 */
@Component
public class OptionsMethodTemplate extends AbstractMethodTemplate {

    public OptionsMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    @Override
    Request forward(Request.Builder builder, String url, Map<String, String> headers, String body) {
        return builder.get().url(url).build();
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.OPTIONS;
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
