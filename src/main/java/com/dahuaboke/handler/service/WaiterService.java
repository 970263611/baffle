package com.dahuaboke.handler.service;

import com.dahuaboke.spring.SpringProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/8/25 17:50
 */
@Component
public class WaiterService {

    @Autowired
    private SpringProperties springProperties;

    public void handle(String uri) {
        try {
            Map<String, Integer> httpUriWaiters = springProperties.getHttpUriWaiters();
            String uriTemp = uri.replaceFirst("/", "");
            if (httpUriWaiters.containsKey(uri)) {
                Integer integer = httpUriWaiters.get(uri);
                Thread.sleep(integer);
            } else if (httpUriWaiters.containsKey(uriTemp)) {
                Integer integer = httpUriWaiters.get(uriTemp);
                Thread.sleep(integer);
            } else {
                int globalHttpWait = springProperties.getGlobalHttpWait();
                if (globalHttpWait != 0) {
                    Thread.sleep(globalHttpWait);
                }
            }
        } catch (InterruptedException e) {
        }
    }
}
