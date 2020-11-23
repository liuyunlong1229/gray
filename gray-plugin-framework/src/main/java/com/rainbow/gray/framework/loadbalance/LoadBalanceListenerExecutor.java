package com.rainbow.gray.framework.loadbalance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;

// 因为内置监听触发的时候，需要优先过滤，所以顺序执行
public class LoadBalanceListenerExecutor {
    @Autowired
    private List<LoadBalanceListener> loadBalanceListenerList;

    private ZoneAwareLoadBalancer<?> loadBalancer;

    public void onGetServers(String serviceId, List<? extends Server> servers) {
        for (LoadBalanceListener loadBalanceListener : loadBalanceListenerList) {
            loadBalanceListener.onGetServers(serviceId, servers);
        }
    }

    public ZoneAwareLoadBalancer<?> getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(ZoneAwareLoadBalancer<?> loadBalancer) {
        this.loadBalancer = loadBalancer;
    }
}