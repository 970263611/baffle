package com.dahuaboke.handler.mode;

import com.dahuaboke.model.BaffleResponse;
import com.dahuaboke.model.HttpTemplateMode;
import com.dahuaboke.model.JsonFileObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author dahua
 * @time 2023/7/20 16:27
 */
@Component
public class FileModeTemplate extends AbstractModeTemplate {

    @Override
    public String readData(JsonFileObject jsonFileObject, HttpTemplateMode httpTemplateMode, String uri, Map<String, String> headers, String body) throws ExecutionException, InterruptedException, JsonProcessingException {
        if (jsonFileObject == null) {
            BaffleResponse proxyMessage = getProxyMessage(httpTemplateMode, uri, headers, body);
            return proxyMessage.getResponse();
        }
        return getFileMessage(jsonFileObject);
    }
}
