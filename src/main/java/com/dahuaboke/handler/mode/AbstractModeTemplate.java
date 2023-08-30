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

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
    private List<String> forwardAddress;

    public AbstractModeTemplate(ModeTemplateFacade modeTemplateFacade) {
        this.register(modeTemplateFacade);
    }

    @PostConstruct
    public void init() {
        List<String> list = Arrays.asList(springProperties.getForwardAddress());
        forwardAddress = list == null ? new ArrayList() : new ArrayList(list);
    }

    public abstract String readData(JsonFileObject jsonFileObject, HttpMethod method, String uri, Map<String, String> headers, String body, long beginTime) throws ExecutionException, InterruptedException, JsonProcessingException, TimeoutException;

    protected abstract BaffleMode baffleMode();

    public void register(ModeTemplateFacade modeTemplateFacade) {
        modeTemplateFacade.register(this, baffleMode());
    }

    protected String getFileMessage(JsonFileObject jsonFileObject, long beginTime) {
        try {
            long nowTime = System.currentTimeMillis();
            long costTime = nowTime - beginTime;
            long globalTimeout = springProperties.getGlobalTimeout();
            if (costTime >= globalTimeout) {
                return BaffleConst.EXCEPTION_TIMEOUT_MESSAGE;
            }
            if (springProperties.getDataCheckMethod() && !jsonFileObject.getType().equals(HttpMethod.GET)) {
                return BaffleConst.EXCEPTION_NOT_ALLOW_METHOD_MESSAGE;
            }
            return objectMapper.writeValueAsString(jsonFileObject.getResponse());
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }


    protected BaffleResponse getProxyMessage(HttpMethod method, String uri, Map<String, String> headers, String body, long beginTime) throws ExecutionException, InterruptedException, JsonProcessingException, TimeoutException {
        String appointIpAndPort = headers.get(BaffleConst.BAFFLE_APPOINT_IP_PORT);
        if (appointIpAndPort == null) {
            Map<String, String> result = new HashMap(forwardAddress.size(), 1);
            for (String host : forwardAddress) {
                BaffleResponse proxyMessage = getProxyMessage(host, uri, method, headers, body, beginTime);
                if (proxyMessage.isSuccess()) {
                    if (!host.equals(forwardAddress.get(0))) {
                        synchronized (this) {
                            forwardAddress.remove(host);
                            forwardAddress.add(0, host);
                        }
                    }
                    return proxyMessage;
                } else {
                    result.put(host + uri, proxyMessage.getResponse());
                }
            }
            return new BaffleResponse(false, objectMapper.writeValueAsString(result));
        } else {
            boolean enableInboundLinks = springProperties.getEnableInboundLinks();
            if (!enableInboundLinks) {
                if (!forwardAddress.contains(appointIpAndPort)) {
                    return new BaffleResponse(false, BaffleConst.EXCEPTION_NOT_ALLOW_RESOURCES_MESSAGE);
                }
            }
            return getProxyMessage(appointIpAndPort, uri, method, headers, body, beginTime);
        }
    }

    private BaffleResponse getProxyMessage(String host, String uri, HttpMethod method, Map<String, String> headers, String body, long beginTime) throws ExecutionException, InterruptedException, TimeoutException {
        if (!host.startsWith(BaffleConst.HTTP_PREFIX) && !host.startsWith(BaffleConst.HTTPS_PREFIX)) {
            host = BaffleConst.HTTP_PREFIX + host;
        }
        if (host.endsWith(BaffleConst.SYMBOL_SLASH)) {
            host = host.substring(0, host.length() - 1);
        }
        return proxyService.proxy(host + uri, method, headers, body, beginTime);
    }
}
