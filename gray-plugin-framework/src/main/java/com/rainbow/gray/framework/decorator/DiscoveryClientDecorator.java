package com.rainbow.gray.framework.decorator;



import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.rainbow.gray.framework.context.PluginContextAware;
import com.rainbow.gray.framework.filter.ServerExecutor;

public class DiscoveryClientDecorator implements DiscoveryClient {

    private DiscoveryClient discoveryClient;
    private ConfigurableApplicationContext applicationContext;
    private ConfigurableEnvironment environment;

    public DiscoveryClientDecorator(DiscoveryClient discoveryClient, ConfigurableApplicationContext applicationContext) {
        this.discoveryClient = discoveryClient;
        this.applicationContext = applicationContext;
        this.environment = applicationContext.getEnvironment();
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        List<ServiceInstance> instances = getRealInstances(serviceId);

        Boolean discoveryControlEnabled = PluginContextAware.isDiscoveryControlEnabled(environment);
        if (discoveryControlEnabled) {
            try {
            	ServerExecutor serverExecutor = applicationContext.getBean(ServerExecutor.class);
            	serverExecutor.onGetInstances(serviceId, instances);
            } catch (BeansException e) {
                // LOG.warn("Get bean for DiscoveryListenerExecutor failed, ignore to executor listener");
            }
        }

        return instances;
    }

    public List<ServiceInstance> getRealInstances(String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }

    @Override
    public List<String> getServices() {
        List<String> services = getRealServices();

        Boolean discoveryControlEnabled = PluginContextAware.isDiscoveryControlEnabled(environment);
        if (discoveryControlEnabled) {
            try {
            	ServerExecutor serverExecutor = applicationContext.getBean(ServerExecutor.class);
            	serverExecutor.onGetServices(services);
            } catch (BeansException e) {
                // LOG.warn("Get bean for DiscoveryListenerExecutor failed, ignore to executor listener");
            }
        }

        return services;
    }

    public List<String> getRealServices() {
        return discoveryClient.getServices();
    }

    @Override
    public String description() {
        return discoveryClient.description();
    }

    public ConfigurableEnvironment getEnvironment() {
        return environment;
    }
}