package com.rainbow.gray.framework.starter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rainbow.gray.framework.adapter.ConfigAdapter;
import com.rainbow.gray.framework.adapter.NacosConfigAdapter;

@Configuration
public class NacosConfigAutoConfiguration {
    
    @Bean
    public ConfigAdapter configAdapter() {
        return new NacosConfigAdapter();
    }
}