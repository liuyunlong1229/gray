package com.rainbow.gray.framework.context;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

import com.rainbow.gray.framework.constant.GrayConstant;

public class PluginEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (StringUtils.equals(environment.getClass().getName(), StandardEnvironment.class.getName())) {
            String uuid = UUID.randomUUID().toString();
            System.setProperty(GrayConstant.SPRING_APPLICATION_UUID, uuid);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}