package com.dahuaboke.handler.net.template;

import com.dahuaboke.model.BaffleConst;
import io.netty.handler.codec.http.HttpMethod;
import okhttp3.FormBody;
import okhttp3.Request;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/19 21:57
 */
@Component
public class PatchFileFormMethodTemplate extends AbstractMethodTemplate {

    public PatchFileFormMethodTemplate(RequestTemplateFacade requestTemplateFacade) {
        super(requestTemplateFacade);
    }

    @Override
    Request forward(Request.Builder builder, String url, Map<String, String> headers, String body) {
        String contentType = headers.get(headerName());
        contentType = contentType == null ? headers.get(headerName().toLowerCase()) : contentType;
        contentType = contentType == null ? headers.get(headerName().toUpperCase()) : contentType;
        String boundary = null;
        for (String s : contentType.split(";")) {
            if (s.contains(BaffleConst.FORM_SPLIT)) {
                boundary = s.split(BaffleConst.FORM_SPLIT)[1];
            }
        }
        if (boundary == null) {
            throw new IllegalArgumentException("异常：请求参数错误，缺少boundary");
        }
        String[] split = body.split(boundary);
        FormBody.Builder builderPostForm = new FormBody.Builder();
        for (int a = 1; a < split.length - 1; a++) {
            String param = split[a].replaceAll("-", "");
            String[] p = param.split("\r\n");
            String k = p[1].split("=")[1].replaceAll("\"", "");
            String v = p[3].trim();
            builderPostForm.add(k, v);
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
        return "multipart/form-data";
    }
}
