package com.dahuaboke.handler.net.template;

import io.netty.handler.codec.http.HttpMethod;
import okhttp3.FormBody;
import okhttp3.Request;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class PatchX3WFormMethodTemplate extends AbstractMethodTemplate {

    public PatchX3WFormMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    @Override
    Request forward(Request.Builder builder, String url, Map<String, String> headers, String body) {
        FormBody.Builder builderPostForm = new FormBody.Builder();
        if (body != null && !"".equals(body)) {
            String[] split = body.split("&");
            for (String s : split) {
                String[] kv = s.split("=");
                builderPostForm.add(decode(kv[0]), decode(kv[1]));
            }
        }
        FormBody formBody = builderPostForm.build();
        return builder.patch(formBody).url(url).build();
    }

    @Override
    protected HttpMethod httpMethod() {
        return HttpMethod.PATCH;
    }

    @Override
    protected String headerValue() {
        return "application/x-www-form-urlencoded";
    }

    private String decode(String str) {
        try {
            if (str == null) {
                return null;
            }
            if (str.contains("%") || str.contains("+")) {
                return URLDecoder.decode(str, "UTF-8");
            }
            return str;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
