package com.rainbow.gray.framework.starter.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nepxion.eventbus.annotation.EnableEventBus;
import com.rainbow.gray.framework.cache.PluginCache;
import com.rainbow.gray.framework.cache.RuleCache;
import com.rainbow.gray.framework.constant.GrayConstant;
import com.rainbow.gray.framework.context.PluginContextAware;
import com.rainbow.gray.framework.event.PluginEventWapper;
import com.rainbow.gray.framework.event.PluginPublisher;
import com.rainbow.gray.framework.event.PluginSubscriber;
import com.rainbow.gray.framework.filter.BlackListServerFilter;
import com.rainbow.gray.framework.filter.RegionServerFilter;
import com.rainbow.gray.framework.filter.ServerExecutor;
import com.rainbow.gray.framework.filter.VersionServerFilter;
import com.rainbow.gray.framework.filter.ZoneServerFilter;
import com.rainbow.gray.framework.loadbalance.BlacklistFilterLoadBalanceListener;
import com.rainbow.gray.framework.loadbalance.LoadBalanceListenerExecutor;
import com.rainbow.gray.framework.loadbalance.RegionFilterLoadBalanceListener;
import com.rainbow.gray.framework.loadbalance.VersionFilterLoadBalanceListener;
import com.rainbow.gray.framework.loadbalance.ZoneFilterLoadBalanceListener;
import com.rainbow.gray.framework.matcher.DiscoveryAntPathMatcherStrategy;
import com.rainbow.gray.framework.matcher.DiscoveryMatcherStrategy;
import com.rainbow.gray.framework.ribbon.RibbonProcessor;

@Configuration
@EnableEventBus
public class PluginAutoConfiguration {
	
    @Bean
    public PluginPublisher pluginPublisher() {
        return new PluginPublisher();
    }

    @Bean
    public PluginSubscriber pluginSubscriber() {
        return new PluginSubscriber();
    }

    @Bean
    public PluginEventWapper pluginEventWapper() {
        return new PluginEventWapper();
    }

    @Bean
    public PluginContextAware pluginContextAware() {
        return new PluginContextAware();
    }
    
    @Bean
    public PluginCache pluginCache() {
        return new PluginCache();
    }

    @Bean
    public RuleCache ruleCache() {
        return new RuleCache();
    }
    
    @Bean
    public RibbonProcessor ribbonProcessor() {
        return new RibbonProcessor();
    }

    @Bean
    public ServerExecutor serverExecutor() {
        return new ServerExecutor();
    }

    @Bean
    public LoadBalanceListenerExecutor loadBalanceListenerExecutor() {
        return new LoadBalanceListenerExecutor();
    }

    @Bean
    @ConditionalOnMissingBean
    public BlackListServerFilter blackListServerFilter() {
        return new BlackListServerFilter();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public VersionServerFilter versionServerFilter() {
        return new VersionServerFilter();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public RegionServerFilter regionServerFilter() {
        return new RegionServerFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = GrayConstant.SPRING_APPLICATION_ZONE_AFFINITY_ENABLED, matchIfMissing = false)
    public ZoneServerFilter zoneServerFilter() {
        return new ZoneServerFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public BlacklistFilterLoadBalanceListener blacklistFilterLoadBalanceListener() {
        return new BlacklistFilterLoadBalanceListener();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public VersionFilterLoadBalanceListener versionFilterLoadBalanceListener() {
        return new VersionFilterLoadBalanceListener();
    }

    @Bean
    @ConditionalOnMissingBean
    public RegionFilterLoadBalanceListener regionFilterLoadBalanceListener() {
        return new RegionFilterLoadBalanceListener();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = GrayConstant.SPRING_APPLICATION_ZONE_AFFINITY_ENABLED, matchIfMissing = false)
    public ZoneFilterLoadBalanceListener zoneFilterLoadBalanceListener() {
        return new ZoneFilterLoadBalanceListener();
    }
   
    @Bean
    @ConditionalOnMissingBean
    public DiscoveryMatcherStrategy discoveryMatcherStrategy() {
        return new DiscoveryAntPathMatcherStrategy();
    }
}