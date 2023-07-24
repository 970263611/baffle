package com.dahuaboke.handler.mode;

import com.dahuaboke.model.BaffleMode;
import com.dahuaboke.model.JsonFileObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author dahua
 * @time 2023/7/24 9:14
 */
@Component
public class ModeTemplateFacade {

    public Map<BaffleMode, AbstractModeTemplate> registerModeTemplate = new HashMap();

    public String readDate(BaffleMode baffleMode, JsonFileObject jsonFileObject, HttpMethod method, String uri, Map<String, String> headers, String body) throws ExecutionException, InterruptedException, JsonProcessingException {
        AbstractModeTemplate abstractModeTemplate = registerModeTemplate.get(baffleMode);
        if (abstractModeTemplate == null) {
            return "异常：请求模式配置错误";
        }
        return abstractModeTemplate.readData(jsonFileObject, method, uri, headers, body);
    }

    public void register(AbstractModeTemplate abstractModeTemplate, BaffleMode baffleMode) {
        registerModeTemplate.put(baffleMode, abstractModeTemplate);
    }
}
