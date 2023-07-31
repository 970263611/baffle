package com.dahuaboke.handler.mode;

import com.dahuaboke.model.BaffleConst;
import com.dahuaboke.model.BaffleMode;
import com.dahuaboke.model.JsonFileObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author dahua
 * @time 2023/7/24 9:14
 */
@Component
public class ModeTemplateFacade {

    public Map<BaffleMode, AbstractModeTemplate> registerModeTemplate = new HashMap();

    public String readDate(BaffleMode baffleMode, JsonFileObject jsonFileObject, HttpMethod method, String uri, Map<String, String> headers, String body, long beginTime) throws ExecutionException, InterruptedException, JsonProcessingException, TimeoutException {
        AbstractModeTemplate abstractModeTemplate = registerModeTemplate.get(baffleMode);
        if (abstractModeTemplate == null) {
            return BaffleConst.EXCEPTION_NOT_ALLOW_METHOD_MESSAGE;
        }
        return abstractModeTemplate.readData(jsonFileObject, method, uri, headers, body, beginTime);
    }

    public void register(AbstractModeTemplate abstractModeTemplate, BaffleMode baffleMode) {
        registerModeTemplate.put(baffleMode, abstractModeTemplate);
    }
}
