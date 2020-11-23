package com.rainbow.gray.framework.context;

import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import com.rainbow.gray.framework.constant.GrayConstant;
import com.rainbow.gray.framework.decorator.DiscoveryClientDecorator;
import com.rainbow.gray.framework.property.DiscoveryProperties;


public abstract class PluginApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(PluginApplicationContextInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!(applicationContext instanceof AnnotationConfigApplicationContext)) {
           
            boolean servletWebServerEnabled = applicationContext.getClass().getName().endsWith(GrayConstant.ANNOTATION_CONFIG_SERVLET_WEB_SERVER_APPLICATION_CONTEXT);
            System.setProperty(GrayConstant.SPRING_APPLICATION_SERVLET_WEB_SERVER_ENABLED, Boolean.toString(servletWebServerEnabled));

            initializeDefaultProperties(applicationContext);
        }

        applicationContext.getBeanFactory().addBeanPostProcessor(new InstantiationAwareBeanPostProcessorAdapter() {
            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof DiscoveryClient) {
                    DiscoveryClient discoveryClient = (DiscoveryClient) bean;

                    return new DiscoveryClientDecorator(discoveryClient, applicationContext);
                } else {
                    return afterInitialization(applicationContext, bean, beanName);
                }
            }
        });
    }

    private void initializeDefaultProperties(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        String path = PluginContextAware.getDefaultPropertiesPath(environment);

        try {
            DiscoveryProperties properties = new DiscoveryProperties(path, GrayConstant.ENCODING_GBK, GrayConstant.ENCODING_UTF_8);
            Map<String, String> propertiesMap = properties.getMap();
            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                // 如果已经设置，则尊重已经设置的值
                if (environment.getProperty(key) == null && System.getProperty(key) == null && System.getenv(key.toUpperCase()) == null) {
                    System.setProperty(key, value);
                }
            }

            LOG.info("{} is loaded...", path);
        } catch (IOException e) {

        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    protected abstract Object afterInitialization(ConfigurableApplicationContext applicationContext, Object bean, String beanName) throws BeansException;
}