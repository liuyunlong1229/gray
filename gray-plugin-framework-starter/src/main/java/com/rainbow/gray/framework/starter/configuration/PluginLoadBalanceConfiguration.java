package com.rainbow.gray.framework.starter.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClientName;
import org.springframework.context.annotation.Bean;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ServerListUpdater;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.rainbow.gray.framework.decorator.ZoneAvoidanceRuleDecorator;
import com.rainbow.gray.framework.loadbalance.LoadBalanceListenerExecutor;

@AutoConfigureAfter(RibbonClientConfiguration.class)
public class PluginLoadBalanceConfiguration {
    @RibbonClientName
    private String serviceId = "client";

    @Autowired
    private PropertiesFactory propertiesFactory;

    @Autowired
    private LoadBalanceListenerExecutor loadBalanceListenerExecutor;

    @Bean
    @ConditionalOnMissingBean
    public IRule ribbonRule(IClientConfig config) {
        if (this.propertiesFactory.isSet(IRule.class, serviceId)) {
            return this.propertiesFactory.get(IRule.class, config, serviceId);
        }

        ZoneAvoidanceRuleDecorator rule = new ZoneAvoidanceRuleDecorator();
        rule.initWithNiwsConfig(config);

        return rule;
    }

    @Bean
    public ILoadBalancer ribbonLoadBalancer(IClientConfig config, ServerList<Server> serverList, ServerListFilter<Server> serverListFilter, IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
        if (this.propertiesFactory.isSet(ILoadBalancer.class, serviceId)) {
            return this.propertiesFactory.get(ILoadBalancer.class, config, serviceId);
        }

        ZoneAwareLoadBalancer<?> loadBalancer = new ZoneAwareLoadBalancer<>(config, rule, ping, serverList, serverListFilter, serverListUpdater);
        loadBalanceListenerExecutor.setLoadBalancer(loadBalancer);

        return loadBalancer;
    }
}