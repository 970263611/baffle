package com.dahuaboke.handler.service;

import com.dahuaboke.handler.net.RequestCallBack;
import com.dahuaboke.handler.net.template.RequestTemplateFacade;
import com.dahuaboke.model.BaffleConst;
import com.dahuaboke.model.BaffleResponse;
import com.dahuaboke.spring.SpringProperties;
import io.netty.handler.codec.http.HttpMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author dahua
 * @time 2023/7/17 10:46
 */
@Component
public class ProxyService {

    @Autowired
    private RequestTemplateFacade requestTemplateFacade;
    @Autowired
    private SpringProperties springProperties;

    public BaffleResponse proxy(String url, HttpMethod method, Map<String, String> headers, String body, long beginTime) throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<BaffleResponse> completableFuture = new CompletableFuture();
        RequestCallBack requestCallBack = (baffleResponse) -> {
            completableFuture.complete(baffleResponse);
        };
        requestTemplateFacade.exec(url, method, headers, body, requestCallBack);
        long globalTimeout = springProperties.getGlobalTimeout();
        long nowTime = System.currentTimeMillis();
        long costTime = nowTime - beginTime;
        if (costTime >= globalTimeout) {
            return new BaffleResponse(false, BaffleConst.EXCEPTION_TIMEOUT_MESSAGE);
        }
        return completableFuture.get(globalTimeout - costTime, TimeUnit.MILLISECONDS);
    }
}
