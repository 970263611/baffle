package com.dahuaboke.handler;

import com.dahuaboke.handler.service.ForwardService;
import com.dahuaboke.handler.service.JsonFileService;
import com.dahuaboke.model.BaffleMode;
import com.dahuaboke.model.JsonFileObject;
import com.dahuaboke.spring.SpringProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/17 10:40
 */
@Component
public class HttpController {

    @Autowired
    private JsonFileService jsonFileService;
    @Autowired
    private ForwardService forwardService;
    @Autowired
    private SpringProperties springProperties;
    private ObjectMapper objectMapper = new ObjectMapper();

    public String handle(HttpMethod method, String uri, Map<String, String> headers, String requestContent, Map<String, String> body) {
        System.out.println(String.format("接入新请求：method：%s，uri：%s，headers：%s，content：%s，body：%s", method, uri, headers, requestContent, body));
        String result = null;
        JsonFileObject jsonFileObject = jsonFileService.getObjByUri(uri);
        //全局
        BaffleMode baffleMode = springProperties.getBaffleMode();
        if (jsonFileObject != null) {
            //覆盖的
            BaffleMode mode = jsonFileObject.getMode();
            if (mode != null) {
                baffleMode = mode;
            }
        }
        switch (baffleMode) {
            case FILE:
                if (jsonFileObject == null) {
                    result = forwardService.forward(method, uri, headers, requestContent, body);
                } else {
                    result = getFileMessage(jsonFileObject);
                }
                break;
            case SERVICE:
                result = forwardService.forward(method, uri, headers, requestContent, body);
                if (result == null) {
                    result = getFileMessage(jsonFileObject);
                }
                break;
            case ONLY_FILE:
                if (jsonFileObject == null) {
                    break;
                } else {
                    result = getFileMessage(jsonFileObject);
                }
                break;
            case ONLY_SERVICE:
                result = forwardService.forward(method, uri, headers, requestContent, body);
                break;
            default:
                break;
        }
        return result;
    }

    public String getFileMessage(JsonFileObject jsonFileObject) {
        try {
            return objectMapper.writeValueAsString(jsonFileObject.getResult());
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }
}
