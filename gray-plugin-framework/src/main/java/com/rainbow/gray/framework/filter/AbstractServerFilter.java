package com.rainbow.gray.framework.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import com.rainbow.gray.framework.adapter.PluginAdapter;
import com.rainbow.gray.framework.context.PluginContextAware;

public abstract class AbstractServerFilter implements ServerFilter {
	
	@Autowired
    protected DiscoveryClient discoveryClient;
	
	@Autowired
    protected PluginContextAware pluginContextAware;

    @Autowired
    protected PluginAdapter pluginAdapter;


    public PluginContextAware getPluginContextAware() {
        return pluginContextAware;
    }

    public PluginAdapter getPluginAdapter() {
        return pluginAdapter;
    }


    public DiscoveryClient getDiscoveryClient() {
        return discoveryClient;
    }

}
