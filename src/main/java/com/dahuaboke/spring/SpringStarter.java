package com.dahuaboke.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author dahua
 * @time 2023/7/17 10:21
 */
public class SpringStarter {

    public void run() {
        new AnnotationConfigApplicationContext(SpringConfig.class);
    }
}
