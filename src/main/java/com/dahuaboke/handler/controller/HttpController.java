package com.dahuaboke.handler.controller;

import com.dahuaboke.handler.mode.FileModeTemplate;
import com.dahuaboke.handler.mode.OnlyFileModeTemplate;
import com.dahuaboke.handler.mode.OnlyProxyModeTemplate;
import com.dahuaboke.handler.mode.ProxyModeTemplate;
import com.dahuaboke.handler.service.FileService;
import com.dahuaboke.model.BaffleMode;
import com.dahuaboke.model.HttpTemplateMode;
import com.dahuaboke.model.JsonFileObject;
import com.dahuaboke.spring.SpringProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author dahua
 * @time 2023/7/17 10:40
 */
@Component
public class HttpController {

    @Autowired
    private FileService fileService;
    @Autowired
    private SpringProperties springProperties;
    @Autowired
    private FileModeTemplate fileModeTemplate;
    @Autowired
    private ProxyModeTemplate proxyModeTemplate;
    @Autowired
    private OnlyFileModeTemplate onlyFileModeTemplate;
    @Autowired
    private OnlyProxyModeTemplate onlyProxyModeTemplate;

    public String handle(FullHttpRequest fullHttpRequest) {
        HttpMethod method = fullHttpRequest.getMethod();
        String uri = fullHttpRequest.getUri();
        Map<String, String> headers = new HashMap();
        Iterator<Map.Entry<String, String>> iterator = fullHttpRequest.headers().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            headers.put(next.getKey(), next.getValue());
        }
        String body = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
        try {
            HttpTemplateMode httpTemplateMode = getHttpTemplateMode(method, headers);
            if (httpTemplateMode == null) {
                return "该请求方式暂不支持";
            }
            return handle(httpTemplateMode, uri, headers, body);
        } catch (Exception e) {
            return e.getCause().toString();
        }
    }

    public String handle(HttpTemplateMode httpTemplateMode, String uri, Map<String, String> headers, String body) throws ExecutionException, InterruptedException, JsonProcessingException {
        String result;
        JsonFileObject jsonFileObject = fileService.getObjByUri(uri);
        BaffleMode baffleMode = springProperties.getBaffleMode();
        if (jsonFileObject != null) {
            BaffleMode mode = jsonFileObject.getMode();
            if (mode != null) {
                baffleMode = mode;
            }
        }
        switch (baffleMode) {
            case FILE:
                result = fileModeTemplate.readData(jsonFileObject, httpTemplateMode, uri, headers, body);
                break;
            case PROXY:
                result = proxyModeTemplate.readData(jsonFileObject, httpTemplateMode, uri, headers, body);
                break;
            case ONLY_FILE:
                result = onlyFileModeTemplate.readData(jsonFileObject, httpTemplateMode, uri, headers, body);
                break;
            case ONLY_PROXY:
                result = onlyProxyModeTemplate.readData(jsonFileObject, httpTemplateMode, uri, headers, body);
                break;
            default:
                result = fileModeTemplate.readData(jsonFileObject, httpTemplateMode, uri, headers, body);
                break;
        }
        return result;
    }

    private HttpTemplateMode getHttpTemplateMode(HttpMethod method, Map<String, String> headers) {
        HttpTemplateMode httpTemplateMode = null;
        switch (method.name()) {
            case "GET":
                httpTemplateMode = HttpTemplateMode.GET;
                break;
            case "POST":
                String contentType = headers.get("Content-Type");
                switch (contentType) {
                    case "multipart/form-data":
                        httpTemplateMode = HttpTemplateMode.POST_FORM;
                        break;
                    case "application/json":
                        httpTemplateMode = HttpTemplateMode.POST_JSON;
                        break;
                    case "application/x-www-form-urlencoded":
                        break;
                    case "text/plain":
                        break;
                    case "application/javascript":
                        break;
                    case "text/xml":
                        break;
                    case "text/html":
                        break;
                    default:
                        break;
                }
            case "PUT":
                break;
            case "PATCH":
                break;
            case "OPTIONS":
                break;
            case "HEAD":
                break;
            case "DELETE":
                break;
            case "TRACE":
                break;
            case "CONNECT":
                break;
            default:
                break;
        }
        return httpTemplateMode;
    }
}
