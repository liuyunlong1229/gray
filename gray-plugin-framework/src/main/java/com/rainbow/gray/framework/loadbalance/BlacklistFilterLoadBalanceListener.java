package com.rainbow.gray.framework.loadbalance;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.loadbalancer.Server;
import com.rainbow.gray.framework.entity.BlacklistEntity;
import com.rainbow.gray.framework.entity.DiscoveryEntity;
import com.rainbow.gray.framework.entity.RuleEntity;
import com.rainbow.gray.framework.matcher.DiscoveryMatcherStrategy;

public class BlacklistFilterLoadBalanceListener extends AbstractLoadBalanceListener {
	
	@Autowired
	private DiscoveryMatcherStrategy discoveryMatcherStrategy;
	
    @Override
    public void onGetServers(String serviceId, List<? extends Server> servers) {
        applyBlacklistFilter(servers);
    }

    private void applyBlacklistFilter(List<? extends Server> servers) {

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

        Iterator<? extends Server> iterator = servers.iterator();
        while (iterator.hasNext()) {
        	Server server = iterator.next();
            if (applyIdBlacklist(server,idList)) {
            	iterator.remove();
			}
            if (applyAddressBlacklist(server,addressList)) {
            	iterator.remove();
			}
        }
    }
    

    public boolean applyIdBlacklist(Server server,List<String> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }

        String serviceUUId = pluginAdapter.getServerServiceUUId(server);

        if (idList.contains(serviceUUId)) {
            return true;
        }

        return false;
    }

    public boolean applyAddressBlacklist(Server server,List<String> addressList) {
        if (CollectionUtils.isEmpty(addressList)) {
            return false;
        }

        // 如果精确匹配不满足，尝试用通配符匹配
        if (addressList.contains(server.getHostPort()) 
        		|| addressList.contains(server.getHost()) || addressList.contains(String.valueOf(server.getPort()))) {
            return true;
        }

        // 通配符匹配。前者是通配表达式，后者是具体值
        for (String addressPattern : addressList) {
            if (discoveryMatcherStrategy.match(addressPattern, server.getHostPort()) 
            		|| discoveryMatcherStrategy.match(addressPattern, server.getHost()) 
            		|| discoveryMatcherStrategy.match(addressPattern, String.valueOf(server.getPort()))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getOrder() {
        // After host filter
        return HIGHEST_PRECEDENCE;
    }
}