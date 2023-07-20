package com.dahuaboke.handler.mode;

import com.dahuaboke.handler.service.ProxyService;
import com.dahuaboke.model.BaffleResponse;
import com.dahuaboke.model.HttpTemplateMode;
import com.dahuaboke.model.JsonFileObject;
import com.dahuaboke.spring.SpringProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author dahua
 * @time 2023/7/20 16:30
 */
public abstract class AbstractModeTemplate {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProxyService proxyService;
    @Autowired
    private SpringProperties springProperties;

    public abstract String readData(JsonFileObject jsonFileObject, HttpTemplateMode httpTemplateMode, String uri, Map<String, String> headers, String body) throws ExecutionException, InterruptedException, JsonProcessingException;

    protected String getFileMessage(JsonFileObject jsonFileObject) {
        try {
            return objectMapper.writeValueAsString(jsonFileObject.getResponse());
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    protected BaffleResponse getProxyMessage(HttpTemplateMode httpTemplateMode, String uri, Map<String, String> headers, String body) throws ExecutionException, InterruptedException, JsonProcessingException {
        Map<String, String> result = new HashMap();
        String[] forwardAddress = springProperties.getForwardAddress();
        for (String host : forwardAddress) {
            if (!host.startsWith("http://") && !host.startsWith("https://")) {
                host = "http://" + host;
            }
            if (host.endsWith("/")) {
                host = host.substring(0, host.length() - 1);
            }
            String url = host + uri;
            BaffleResponse proxy = proxyService.proxy(url, httpTemplateMode, headers, body);
            if (proxy.isSuccess()) {
                return proxy;
            } else {
                result.put(url, proxy.getResponse());
            }
        }
        return new BaffleResponse(false, "请求失败，错误信息 -> " + objectMapper.writeValueAsString(result));
    }
}
