package com.rainbow.gray.framework.context;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.rainbow.gray.framework.constant.GrayConstant;
import com.rainbow.gray.framework.constant.NacosConstant;
import com.rainbow.gray.framework.utils.MetadataUtil;

public class NacosApplicationContextInitializer extends PluginApplicationContextInitializer {
    @Override
    protected Object afterInitialization(ConfigurableApplicationContext applicationContext, Object bean, String beanName) throws BeansException {
        if (bean instanceof NacosDiscoveryProperties) {
            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            NacosDiscoveryProperties nacosDiscoveryProperties = (NacosDiscoveryProperties) bean;

            Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();

            String groupKey = PluginContextAware.getGroupKey(environment);
            if (!metadata.containsKey(groupKey)) {
                metadata.put(groupKey, GrayConstant.DEFAULT);
            }
            if (!metadata.containsKey(GrayConstant.VERSION)) {
                metadata.put(GrayConstant.VERSION, GrayConstant.DEFAULT);
            }
            if (!metadata.containsKey(GrayConstant.REGION)) {
                metadata.put(GrayConstant.REGION, GrayConstant.DEFAULT);
            }

            metadata.put(GrayConstant.SPRING_BOOT_VERSION, SpringBootVersion.getVersion());
            metadata.put(GrayConstant.SPRING_APPLICATION_NAME, PluginContextAware.getApplicationName(environment));
            metadata.put(GrayConstant.SPRING_APPLICATION_TYPE, PluginContextAware.getApplicationType(environment));
            metadata.put(GrayConstant.SPRING_APPLICATION_UUID, PluginContextAware.getApplicationUUId(environment));
            metadata.put(GrayConstant.SPRING_APPLICATION_DISCOVERY_PLUGIN, NacosConstant.NACOS_TYPE);
            metadata.put(GrayConstant.SPRING_APPLICATION_DISCOVERY_VERSION, GrayConstant.GRAY_VERSION);
            String agentVersion = System.getProperty(GrayConstant.SPRING_APPLICATION_DISCOVERY_AGENT_VERSION);
            metadata.put(GrayConstant.SPRING_APPLICATION_DISCOVERY_AGENT_VERSION, StringUtils.isEmpty(agentVersion) ? GrayConstant.UNKNOWN : agentVersion);
            metadata.put(GrayConstant.SPRING_APPLICATION_REGISTER_CONTROL_ENABLED, PluginContextAware.isRegisterControlEnabled(environment).toString());
            metadata.put(GrayConstant.SPRING_APPLICATION_DISCOVERY_CONTROL_ENABLED, PluginContextAware.isDiscoveryControlEnabled(environment).toString());
            metadata.put(GrayConstant.SPRING_APPLICATION_CONFIG_REST_CONTROL_ENABLED, PluginContextAware.isConfigRestControlEnabled(environment).toString());
            metadata.put(GrayConstant.SPRING_APPLICATION_GROUP_KEY, groupKey);
            metadata.put(GrayConstant.SPRING_APPLICATION_CONTEXT_PATH, PluginContextAware.getContextPath(environment));

            MetadataUtil.filter(metadata);

            return bean;
        } else {
            return bean;
        }
    }
}