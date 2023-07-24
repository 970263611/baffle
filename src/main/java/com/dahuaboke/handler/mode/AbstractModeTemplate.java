package com.dahuaboke.handler.mode;

import com.dahuaboke.handler.service.ProxyService;
import com.dahuaboke.model.BaffleConst;
import com.dahuaboke.model.BaffleMode;
import com.dahuaboke.model.BaffleResponse;
import com.dahuaboke.model.JsonFileObject;
import com.dahuaboke.spring.SpringProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    public AbstractModeTemplate(ModeTemplateFacade modeTemplateFacade) {
        this.register(modeTemplateFacade);
    }

    public abstract String readData(JsonFileObject jsonFileObject, HttpMethod method, String uri, Map<String, String> headers, String body) throws ExecutionException, InterruptedException, JsonProcessingException;

    protected abstract BaffleMode baffleMode();

    public void register(ModeTemplateFacade modeTemplateFacade) {
        modeTemplateFacade.register(this, baffleMode());
    }

    protected String getFileMessage(JsonFileObject jsonFileObject) {
        try {
            return objectMapper.writeValueAsString(jsonFileObject.getResponse());
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    protected BaffleResponse getProxyMessage(HttpMethod method, String uri, Map<String, String> headers, String body) throws ExecutionException, InterruptedException, JsonProcessingException {
        Map<String, String> result = new HashMap();
        List<String> forwardAddress = Arrays.asList(springProperties.getForwardAddress());
        if (headers.containsKey(BaffleConst.BAFFLE_APPOINT_IP) || headers.containsKey(BaffleConst.BAFFLE_APPOINT_IP.toUpperCase())) {
            String appointIp = headers.get(BaffleConst.BAFFLE_APPOINT_IP);
            appointIp = appointIp == null ? headers.get(BaffleConst.BAFFLE_APPOINT_IP.toUpperCase()) : appointIp;
            if (forwardAddress.contains(appointIp)) {
                return getProxyMessage(appointIp, uri, method, headers, body);
            }
        }
        for (String host : forwardAddress) {
            BaffleResponse proxyMessage = getProxyMessage(host, uri, method, headers, body);
            if (proxyMessage.isSuccess()) {
                return proxyMessage;
            } else {
                result.put(host + uri, proxyMessage.getResponse());
            }
        }
        return new BaffleResponse(false, "请求失败，错误信息 -> " + objectMapper.writeValueAsString(result));
    }

    private BaffleResponse getProxyMessage(String host, String uri, HttpMethod method, Map<String, String> headers, String body) throws ExecutionException, InterruptedException, JsonProcessingException {
        if (!host.startsWith(BaffleConst.HTTP_PREFIX) && !host.startsWith(BaffleConst.HTTPS_PREFIX)) {
            host = BaffleConst.HTTP_PREFIX + host;
        }
        if (host.endsWith(BaffleConst.SYMBOL_SLASH)) {
            host = host.substring(0, host.length() - 1);
        }
        return proxyService.proxy(host + uri, method, headers, body);
    }
}
