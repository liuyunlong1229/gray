package com.rainbow.gray.framework.starter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosRibbonClientConfiguration;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;
import com.rainbow.gray.framework.decorator.NacosServerListDecorator;
import com.rainbow.gray.framework.loadbalance.LoadBalanceListenerExecutor;

@AutoConfigureAfter(NacosRibbonClientConfiguration.class)
public class NacosLoadBalanceConfiguration {

    @Autowired
    private ConfigurableEnvironment environment;

    @Autowired
    private LoadBalanceListenerExecutor loadBalanceListenerExecutor;

    @Bean
    public ServerList<?> ribbonServerList(IClientConfig config, NacosDiscoveryProperties nacosDiscoveryProperties) {
        NacosServerListDecorator serverList = new NacosServerListDecorator(nacosDiscoveryProperties);
        serverList.initWithNiwsConfig(config);
        serverList.setEnvironment(environment);
        serverList.setLoadBalanceListenerExecutor(loadBalanceListenerExecutor);

        return serverList;
    }
}