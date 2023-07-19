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

    public String handle(HttpMethod method, String uri, Map<String, String> headers, String body) {
        System.out.println(String.format("接入新请求：method：%s，uri：%s，headers：%s，body：%s", method, uri, headers, body));
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
                    System.out.println("文件中未找到，请求后端获取数据");
                    result = forwardService.forward(method, uri, headers, body);
                } else {
                    result = getFileMessage(jsonFileObject);
                    System.out.println("文件中存在，返回结果 -> " + result);
                }
                break;
            case SERVICE:
                result = forwardService.forward(method, uri, headers, body);
                if (result == null) {
                    System.out.println("后端服务未成功返回，寻找文件数据");
                    result = getFileMessage(jsonFileObject);
                    System.out.println("文件中存在，返回结果 -> " + result);
                }
                break;
            case ONLY_FILE:
                if (jsonFileObject == null) {
                    System.out.println("文件中未找到，请求失败");
                    break;
                } else {
                    result = getFileMessage(jsonFileObject);
                    System.out.println("文件中存在，返回结果 -> " + result);
                }
                break;
            case ONLY_SERVICE:
                result = forwardService.forward(method, uri, headers, body);
                System.out.println("后端服务返回 -> " + result);
                break;
            default:
                result = getFileMessage(jsonFileObject);
                if (result == null) {
                    System.out.println("文件中未找到，请求后端获取数据");
                    result = forwardService.forward(method, uri, headers, body);
                    System.out.println("后端服务返回 -> " + result);
                } else {
                    System.out.println("文件中存在，返回结果 -> " + result);
                }
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
