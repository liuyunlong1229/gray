package com.rainbow.gray.framework.starter.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rainbow.gray.framework.config.PluginConfigParser;
import com.rainbow.gray.framework.constant.GrayConstant;
import com.rainbow.gray.framework.context.ConfigContextClosedHandler;
import com.rainbow.gray.framework.context.PluginContextAware;
import com.rainbow.gray.framework.exception.GrayException;
import com.rainbow.gray.framework.initializer.ConfigInitializer;
import com.rainbow.gray.framework.loader.LocalConfigLoader;
import com.rainbow.gray.framework.parser.xml.XmlConfigParser;

@Configuration
public class ConfigAutoConfiguration {
    @Autowired
    private PluginContextAware pluginContextAware;

    @Bean
    public PluginConfigParser pluginConfigParser() {
        String configFormat = pluginContextAware.getConfigFormat();
        if (StringUtils.equals(configFormat, GrayConstant.XML_FORMAT)) {
            return new XmlConfigParser();
        }

        throw new GrayException("Invalid config format for '" + configFormat + "'");
    }

    @Bean
    public LocalConfigLoader localConfigLoader() {
        return new LocalConfigLoader() {
            @Override
            protected String getPath() {
                return pluginContextAware.getConfigPath();
            }
        };
    }

    @Bean
    public ConfigInitializer configInitializer() {
        return new ConfigInitializer();
    }

    @Bean
    public ConfigContextClosedHandler configContextClosedHandler() {
        return new ConfigContextClosedHandler();
    }
}