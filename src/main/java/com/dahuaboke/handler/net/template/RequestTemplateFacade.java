package com.dahuaboke.handler.net.template;

import com.dahuaboke.handler.net.RequestCallBack;
import com.dahuaboke.model.HttpTemplateMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/20 10:25
 */
@Component
public class RequestTemplateFacade {

    @Autowired
    private GetTemplate getTemplate;
    @Autowired
    private PostBodyTemplate postBodyTemplate;
    @Autowired
    private PostFormTemplate postFormTemplate;

    public void exec(String url, HttpTemplateMode httpTemplateMode, Map<String, String> headers, String body, RequestCallBack requestCallBack) throws JsonProcessingException {
        switch (httpTemplateMode) {
            case GET:
                getTemplate.exec(url, headers, body, requestCallBack);
                break;
            case POST_BODY:
                postBodyTemplate.exec(url, headers, body, requestCallBack);
                break;
            case POST_FORM:
                postFormTemplate.exec(url, headers, body, requestCallBack);
                break;
            default:
                getTemplate.exec(url, headers, body, requestCallBack);
                break;
        }
    }
}
