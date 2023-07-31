package com.dahuaboke.handler.mode;

import com.dahuaboke.model.BaffleMode;
import com.dahuaboke.model.JsonFileObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author dahua
 * @time 2023/7/20 16:28
 */
@Component
public class OnlyProxyModeTemplate extends AbstractModeTemplate {

    public OnlyProxyModeTemplate(ModeTemplateFacade modeTemplateFacade) {
        super(modeTemplateFacade);
    }

    @Override
    public String readData(JsonFileObject jsonFileObject, HttpMethod method, String uri, Map<String, String> headers, String body, long beginTime) throws ExecutionException, InterruptedException, JsonProcessingException, TimeoutException {
        return getProxyMessage(method, uri, headers, body, beginTime).getResponse();
    }

    @Override
    protected BaffleMode baffleMode() {
        return BaffleMode.ONLY_PROXY;
    }
}
