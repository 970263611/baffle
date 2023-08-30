package com.dahuaboke.handler.mode;

import com.dahuaboke.model.BaffleMode;
import com.dahuaboke.model.JsonFileObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author dahua
 * @time 2023/7/20 16:27
 */
@Component
public class OnlyFileModeTemplate extends AbstractModeTemplate {

    public OnlyFileModeTemplate(ModeTemplateFacade modeTemplateFacade) {
        super(modeTemplateFacade);
    }

    @Override
    public String readData(JsonFileObject jsonFileObject, HttpMethod method, String uri, Map<String, String> headers, String body, long beginTime) throws ExecutionException, InterruptedException, JsonProcessingException {
        if (jsonFileObject != null) {
            return getFileMessage(jsonFileObject, beginTime);
        }
        return null;
    }

    @Override
    protected BaffleMode baffleMode() {
        return BaffleMode.ONLY_FILE;
    }
}
