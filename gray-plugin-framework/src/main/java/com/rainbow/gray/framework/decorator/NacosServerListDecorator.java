package com.rainbow.gray.framework.decorator;

import java.util.List;

import org.springframework.core.env.ConfigurableEnvironment;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.cloud.nacos.ribbon.NacosServerList;
import com.rainbow.gray.framework.context.PluginContextAware;
import com.rainbow.gray.framework.loadbalance.LoadBalanceListenerExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class NacosServerListDecorator extends NacosServerList {
    private ConfigurableEnvironment environment;

    private LoadBalanceListenerExecutor loadBalanceListenerExecutor;

    public NacosServerListDecorator(NacosDiscoveryProperties nacosDiscoveryProperties) {
        super(nacosDiscoveryProperties);
    }

    @Override
    public List<NacosServer> getInitialListOfServers() {
        List<NacosServer> servers = super.getInitialListOfServers();

        filter(servers);

        return servers;
    }

    @Override
    public List<NacosServer> getUpdatedListOfServers() {

        List<NacosServer> servers = super.getUpdatedListOfServers();
        RequestAttributes requestAttributes=RequestContextHolder.getRequestAttributes();
        System.out.println("reqeustAtribute=="+requestAttributes);
        filter(servers);

        return servers;
    }

    private void filter(List<NacosServer> servers) {
        Boolean discoveryControlEnabled = PluginContextAware.isDiscoveryControlEnabled(environment);
        if (discoveryControlEnabled) {
            String serviceId = getServiceId();
            loadBalanceListenerExecutor.onGetServers(serviceId, servers);
        }
    }

    public void setEnvironment(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    public void setLoadBalanceListenerExecutor(LoadBalanceListenerExecutor loadBalanceListenerExecutor) {
        this.loadBalanceListenerExecutor = loadBalanceListenerExecutor;
    }
}