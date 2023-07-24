package com.dahuaboke.handler.net.template;

import com.dahuaboke.model.HttpTemplateMode;
import io.netty.handler.codec.http.HttpMethod;
import okhttp3.Request;
import org.springframework.stereotype.Component;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class GetMethodTemplate extends AbstractMethodTemplate {

    public GetMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    @Override
    public Request forward(Request.Builder builder, String url, String body) {
        return builder.get().url(url).build();
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.GET;
    }

    @Override
    protected HttpTemplateMode httpTemplateMode() {
        return HttpTemplateMode.GET;
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
