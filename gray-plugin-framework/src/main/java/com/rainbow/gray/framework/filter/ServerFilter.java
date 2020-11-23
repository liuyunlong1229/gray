package com.rainbow.gray.framework.filter;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.Ordered;

public interface ServerFilter extends Ordered {

	void onGetInstances(String serviceId, List<ServiceInstance> instances);

    void onGetServices(List<String> services);
    
}
