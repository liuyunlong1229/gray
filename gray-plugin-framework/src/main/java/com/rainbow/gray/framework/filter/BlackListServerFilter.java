package com.rainbow.gray.framework.filter;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;

import com.rainbow.gray.framework.entity.BlacklistEntity;
import com.rainbow.gray.framework.entity.DiscoveryEntity;
import com.rainbow.gray.framework.entity.RuleEntity;
import com.rainbow.gray.framework.matcher.DiscoveryMatcherStrategy;

public class BlackListServerFilter extends AbstractServerFilter {
	
	@Autowired
	private DiscoveryMatcherStrategy discoveryMatcherStrategy;
	
    @Override
    public void onGetInstances(String serviceId, List<ServiceInstance> instances) {
    	applyFilter(serviceId, instances);
    }

    private void applyFilter(String providerServiceId, List<ServiceInstance> instances) {
        RuleEntity ruleEntity = pluginAdapter.getRule();
        if (ruleEntity == null) {
            return;
        }

        DiscoveryEntity discoveryEntity = ruleEntity.getDiscoveryEntity();
        if (discoveryEntity == null) {
            return;
        }

        BlacklistEntity blacklistEntity = discoveryEntity.getBlacklistEntity();
        if (blacklistEntity == null) {
            return;
        }

        List<String> addressList = blacklistEntity.getAddressList();
        List<String> idList = blacklistEntity.getIdList();

        if (CollectionUtils.isEmpty(addressList) && CollectionUtils.isEmpty(idList)) {
            return;
        }

        Iterator<ServiceInstance> iterator = instances.iterator();
        while (iterator.hasNext()) {
            ServiceInstance instance = iterator.next();
            if (applyIdBlacklist(instance,idList)) {
            	iterator.remove();
			}
            if (applyAddressBlacklist(instance,addressList)) {
            	iterator.remove();
			}
        }
    }
    

    public boolean applyIdBlacklist(ServiceInstance serviceInstance,List<String> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }

        String serviceUUId = pluginAdapter.getInstanceServiceUUId(serviceInstance);

        if (idList.contains(serviceUUId)) {
            return true;
        }

        return false;
    }

    public boolean applyAddressBlacklist(ServiceInstance serviceInstance,List<String> addressList) {
        if (CollectionUtils.isEmpty(addressList)) {
            return false;
        }

        // 如果精确匹配不满足，尝试用通配符匹配
        if (addressList.contains(serviceInstance.getHost() + ":" + serviceInstance.getPort()) 
        		|| addressList.contains(serviceInstance.getHost()) || addressList.contains(String.valueOf(serviceInstance.getPort()))) {
            return true;
        }

        // 通配符匹配。前者是通配表达式，后者是具体值
        for (String addressPattern : addressList) {
            if (discoveryMatcherStrategy.match(addressPattern, serviceInstance.getHost() + ":" + serviceInstance.getPort()) 
            		|| discoveryMatcherStrategy.match(addressPattern, serviceInstance.getHost()) 
            		|| discoveryMatcherStrategy.match(addressPattern, String.valueOf(serviceInstance.getPort()))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onGetServices(List<String> services) {

    }

    @Override
    public int getOrder() {
        // Highest priority
        return HIGHEST_PRECEDENCE;
    }
}