package com.dahuaboke.handler.service;

import com.dahuaboke.handler.net.RequestCallBack;
import com.dahuaboke.handler.net.template.RequestTemplateFacade;
import com.dahuaboke.model.BaffleResponse;
import com.dahuaboke.model.HttpTemplateMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author dahua
 * @time 2023/7/17 10:46
 */
@Component
public class ProxyService {

    @Autowired
    private RequestTemplateFacade requestTemplateFacade;

    public BaffleResponse proxy(String url, HttpTemplateMode httpTemplateMode, Map<String, String> headers, String body) throws ExecutionException, InterruptedException, JsonProcessingException {
        System.out.println(String.format("转发请求：url：%s，method：%s，headers：%s，body：%s", url, httpTemplateMode, headers, body));
        CompletableFuture<BaffleResponse> completableFuture = new CompletableFuture();
        RequestCallBack requestCallBack = (baffleResponse) -> {
            System.out.println("请求结果：" + baffleResponse);
            completableFuture.complete(baffleResponse);
        };
        requestTemplateFacade.exec(url, httpTemplateMode, headers, body, requestCallBack);
        return completableFuture.get();
    }
}
