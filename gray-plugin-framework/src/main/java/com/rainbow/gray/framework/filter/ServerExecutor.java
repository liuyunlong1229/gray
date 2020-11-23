package com.rainbow.gray.framework.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;

public class ServerExecutor {
	
	@Autowired
    private List<ServerFilter> serverFilterList;

    public void onGetInstances(String serviceId, List<ServiceInstance> instances) {
        for (ServerFilter discoveryListener : serverFilterList) {
            discoveryListener.onGetInstances(serviceId, instances);
        }
    }

    public void onGetServices(List<String> services) {
        for (ServerFilter serverFilter : serverFilterList) {
        	serverFilter.onGetServices(services);
        }
    }

}
