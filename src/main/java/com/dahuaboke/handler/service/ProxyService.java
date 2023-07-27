package com.dahuaboke.handler.service;

import com.dahuaboke.handler.net.RequestCallBack;
import com.dahuaboke.handler.net.template.RequestTemplateFacade;
import com.dahuaboke.model.BaffleResponse;
import io.netty.handler.codec.http.HttpMethod;
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

    public BaffleResponse proxy(String url, HttpMethod method, Map<String, String> headers, String body) throws ExecutionException, InterruptedException {
        System.out.println(String.format("\r\n转发请求：url：%s，method：%s，headers：%s", url, method, headers));
        CompletableFuture<BaffleResponse> completableFuture = new CompletableFuture();
        RequestCallBack requestCallBack = (baffleResponse) -> {
            System.out.println("请求结果：" + baffleResponse.isSuccess());
            System.out.println("------------------------------------------------------------------\r\n");
            completableFuture.complete(baffleResponse);
        };
        requestTemplateFacade.exec(url, method, headers, body, requestCallBack);
        return completableFuture.get();
    }
}
