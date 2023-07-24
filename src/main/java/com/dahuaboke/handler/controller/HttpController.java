package com.dahuaboke.handler.controller;

import com.dahuaboke.handler.mode.ModeTemplateFacade;
import com.dahuaboke.handler.service.FileService;
import com.dahuaboke.model.BaffleMode;
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
    private ModeTemplateFacade modeTemplateFacade;

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
            return handle(method, uri, headers, body);
        } catch (Exception e) {
            return e.getCause().toString();
        }
    }

    public String handle(HttpMethod method, String uri, Map<String, String> headers, String body) throws ExecutionException, InterruptedException, JsonProcessingException {
        JsonFileObject jsonFileObject = fileService.getObjByUri(uri);
        BaffleMode baffleMode = springProperties.getBaffleMode();
        if (jsonFileObject != null) {
            BaffleMode mode = jsonFileObject.getMode();
            if (mode != null) {
                baffleMode = mode;
            }
        }
        return modeTemplateFacade.readDate(baffleMode, jsonFileObject, method, uri, headers, body);
    }
}
