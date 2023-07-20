package com.dahuaboke.spring;

import com.dahuaboke.model.BaffleConst;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.util.Properties;

/**
 * @author dahua
 * @time 2023/7/17 10:23
 */
@Configuration
@ComponentScan("com.dahuaboke.handler")
@PropertySource(value = {"classpath:application.yml"}, factory = SpringConfig.class)
public class SpringConfig extends DefaultPropertySourceFactory {

    @Bean
    public SpringBeanUtil springBeanUtil() {
        return new SpringBeanUtil();
    }

    @Bean
    public SpringProperties springProperties() {
        return new SpringProperties();
    }

    @Bean
    @Order
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    public org.springframework.core.env.PropertySource createPropertySource(String name, EncodedResource resource) throws IOException {
        String sourceName = name != null ? name : resource.getResource().getFilename();
        if (!resource.getResource().exists()) {
            return new PropertiesPropertySource(sourceName, new Properties());
        } else if (sourceName.endsWith(BaffleConst.SUFFIX_YML) || sourceName.endsWith(BaffleConst.SUFFIX_YAML)) {
            Properties propertiesFromYaml = loadYml(resource);
            return new PropertiesPropertySource(sourceName, propertiesFromYaml);
        } else {
            return super.createPropertySource(name, resource);
        }
    }

    private Properties loadYml(EncodedResource resource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
