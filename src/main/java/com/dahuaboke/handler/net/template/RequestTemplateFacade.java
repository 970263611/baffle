package com.dahuaboke.handler.net.template;

import com.dahuaboke.handler.net.RequestCallBack;
import com.dahuaboke.model.BaffleResponse;
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
                String value = headers.get(headerName.toLowerCase());
                String headerValue = registerModel.getHeaderValue();
                AbstractMethodTemplate abstractMethodTemplate = registerModel.getAbstractMethodTemplate();
                if ((value != null && (headerValue.equalsIgnoreCase(value) || value.startsWith(headerValue)))
                        || HttpMethod.GET.equals(method)) {
                    abstractMethodTemplate.exec(url, headers, body, requestCallBack);
                    return;
                } else if (value == null) {
                    boolean defaultMethodTemplate = registerModel.isDefaultMethodTemplate();
                    if (defaultMethodTemplate) {
                        abstractMethodTemplate.exec(url, headers, body, requestCallBack);
                        return;
                    }
                }
            }
        }
        requestCallBack.complate(new BaffleResponse(false, "异常：该请求方式暂不支持"));
    }

    public void register(AbstractMethodTemplate abstractMethodTemplate, HttpMethod httpMethod, String headerName, String headerValue, boolean defaultMethodTemplate) {
        List<RegisterModel> registerModels = registerModeTemplate.get(httpMethod);
        if (registerModels == null) {
            registerModels = new ArrayList();

        }
        registerModels.add(new RegisterModel(abstractMethodTemplate, headerName, headerValue, defaultMethodTemplate));
        registerModeTemplate.put(httpMethod, registerModels);
    }

    class RegisterModel {

        private AbstractMethodTemplate abstractMethodTemplate;
        private String headerName;
        private String headerValue;
        private boolean defaultMethodTemplate;

        public RegisterModel(AbstractMethodTemplate abstractMethodTemplate, String headerName, String headerValue, boolean defaultMethodTemplate) {
            this.abstractMethodTemplate = abstractMethodTemplate;
            this.headerName = headerName;
            this.headerValue = headerValue;
            this.defaultMethodTemplate = defaultMethodTemplate;
        }

        public AbstractMethodTemplate getAbstractMethodTemplate() {
            return abstractMethodTemplate;
        }

        public void setAbstractMethodTemplate(AbstractMethodTemplate abstractMethodTemplate) {
            this.abstractMethodTemplate = abstractMethodTemplate;
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

        public boolean isDefaultMethodTemplate() {
            return defaultMethodTemplate;
        }

        public void setDefaultMethodTemplate(boolean defaultMethodTemplate) {
            this.defaultMethodTemplate = defaultMethodTemplate;
        }
    }
}
