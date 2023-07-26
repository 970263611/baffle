package com.dahuaboke.handler.net.template;

import com.dahuaboke.handler.net.RequestCallBack;
import com.dahuaboke.model.BaffleResponse;
import com.dahuaboke.model.HttpTemplateMode;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/20 10:25
 */
@Component
public class RequestTemplateFacade {

    private Map<HttpMethod, List<RegisterModel>> registerModeTemplate = new HashMap();

    public void exec(String url, HttpMethod method, Map<String, String> headers, String body, RequestCallBack requestCallBack) {
        List<RegisterModel> registerModels = registerModeTemplate.get(method);
        if (registerModels != null) {
            for (RegisterModel registerModel : registerModels) {
                String headerName = registerModel.getHeaderName();
                String value = headers.get(headerName);
                value = value == null ? headers.get(headerName.toLowerCase()) : value;
                value = value == null ? headers.get(headerName.toUpperCase()) : value;
                String headerValue = registerModel.getHeaderValue();
                if ((value != null && (headerValue.equalsIgnoreCase(value) || value.startsWith(headerValue)))
                        || HttpMethod.GET.equals(method)) {
                    AbstractMethodTemplate abstractMethodTemplate = registerModel.getAbstractMethodTemplate();
                    abstractMethodTemplate.exec(url, headers, body, requestCallBack);
                    return;
                }
            }
        }
        requestCallBack.complate(new BaffleResponse(false, "异常：该请求方式暂不支持"));
    }

    public void register(AbstractMethodTemplate abstractMethodTemplate, HttpMethod httpMethod, HttpTemplateMode httpTemplateMode, String headerName, String headerValue) {
        List<RegisterModel> registerModels = registerModeTemplate.get(httpMethod);
        if (registerModels == null) {
            registerModels = new ArrayList();

        }
        registerModels.add(new RegisterModel(abstractMethodTemplate, httpTemplateMode, headerName, headerValue));
        registerModeTemplate.put(httpMethod, registerModels);
    }

    class RegisterModel {

        private AbstractMethodTemplate abstractMethodTemplate;
        private HttpTemplateMode httpTemplateMode;
        private String headerName;
        private String headerValue;

        public RegisterModel(AbstractMethodTemplate abstractMethodTemplate, HttpTemplateMode httpTemplateMode, String headerName, String headerValue) {
            this.abstractMethodTemplate = abstractMethodTemplate;
            this.httpTemplateMode = httpTemplateMode;
            this.headerName = headerName;
            this.headerValue = headerValue;
        }

        public AbstractMethodTemplate getAbstractMethodTemplate() {
            return abstractMethodTemplate;
        }

        public void setAbstractMethodTemplate(AbstractMethodTemplate abstractMethodTemplate) {
            this.abstractMethodTemplate = abstractMethodTemplate;
        }

        public HttpTemplateMode getHttpTemplateMode() {
            return httpTemplateMode;
        }

        public void setHttpTemplateMode(HttpTemplateMode httpTemplateMode) {
            this.httpTemplateMode = httpTemplateMode;
        }

        public String getHeaderName() {
            return headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }

        public String getHeaderValue() {
            return headerValue;
        }

        public void setHeaderValue(String headerValue) {
            this.headerValue = headerValue;
        }
    }
}
