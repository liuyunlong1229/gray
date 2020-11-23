package com.rainbow.gray.framework.ribbon;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.rainbow.gray.framework.loadbalance.LoadBalanceListenerExecutor;

public class RibbonProcessor {
    @Autowired
    private LoadBalanceListenerExecutor loadBalanceListenerExecutor;

    public void refreshLoadBalancer() {
        ZoneAwareLoadBalancer<?> loadBalancer = loadBalanceListenerExecutor.getLoadBalancer();
        if (loadBalancer == null) {
            return;
        }

        loadBalancer.updateListOfServers();
    }
}