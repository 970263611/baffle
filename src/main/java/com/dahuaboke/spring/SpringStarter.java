package com.dahuaboke.spring;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author dahua
 * @time 2023/7/17 10:21
 */
public class SpringStarter {

    public void run() {
        new AnnotationConfigApplicationContext(SpringConfig.class);
        ConfigurableEnvironment environment = null;
    }
}
