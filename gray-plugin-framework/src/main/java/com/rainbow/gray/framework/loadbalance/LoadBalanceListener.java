package com.rainbow.gray.framework.loadbalance;



import java.util.List;

import org.springframework.core.Ordered;

import com.netflix.loadbalancer.Server;

public interface LoadBalanceListener extends Ordered {
    void onGetServers(String serviceId, List<? extends Server> servers);
}