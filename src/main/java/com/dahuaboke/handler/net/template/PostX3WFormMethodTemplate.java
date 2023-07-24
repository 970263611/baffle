package com.dahuaboke.handler.net.template;

import com.dahuaboke.model.HttpTemplateMode;
import io.netty.handler.codec.http.HttpMethod;
import okhttp3.FormBody;
import okhttp3.Request;
import org.springframework.stereotype.Component;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class PostX3WFormMethodTemplate extends AbstractMethodTemplate {

    public PostX3WFormMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    @Override
    Request forward(Request.Builder builder, String url, String body) {
        FormBody.Builder builderPostForm = new FormBody.Builder();
        if (body != null && !"".equals(body)) {
            String[] split = body.split("&");
            for (String s : split) {
                String[] kv = s.split("=");
                builderPostForm.add(kv[0], kv[1]);
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
        return "application/x-www-from-urlencoded";
    }
}
