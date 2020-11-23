package com.rainbow.gray.framework.loadbalance;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.netflix.loadbalancer.Server;
import com.rainbow.gray.framework.entity.DiscoveryEntity;
import com.rainbow.gray.framework.entity.RuleEntity;
import com.rainbow.gray.framework.entity.VersionEntity;
import com.rainbow.gray.framework.entity.VersionFilterEntity;

public class VersionFilterLoadBalanceListener extends AbstractLoadBalanceListener {
    @Override
    public void onGetServers(String serviceId, List<? extends Server> servers) {
        String consumerServiceId = pluginAdapter.getServiceId();
        String consumerServiceVersion = pluginAdapter.getVersion();

        applyVersionFilter(consumerServiceId, consumerServiceVersion, serviceId, servers);
    }

    private void applyVersionFilter(String consumerServiceId, String consumerServiceVersion, String providerServiceId, List<? extends Server> servers) {
        // 如果消费端未配置版本号，那么它可以调用提供端所有服务，需要符合规范，极力避免该情况发生
        if (StringUtils.isEmpty(consumerServiceVersion)) {
            return;
        }

        RuleEntity ruleEntity = pluginAdapter.getRule();
        if (ruleEntity == null) {
            return;
        }

        DiscoveryEntity discoveryEntity = ruleEntity.getDiscoveryEntity();
        if (discoveryEntity == null) {
            return;
        }

        VersionFilterEntity versionFilterEntity = discoveryEntity.getVersionFilterEntity();
        if (versionFilterEntity == null) {
            return;
        }

        Map<String, List<VersionEntity>> versionEntityMap = versionFilterEntity.getVersionEntityMap();
        if (MapUtils.isEmpty(versionEntityMap)) {
            return;
        }

        List<VersionEntity> versionEntityList = versionEntityMap.get(consumerServiceId);
        if (CollectionUtils.isEmpty(versionEntityList)) {
            return;
        }

        // 当前版本的消费端所能调用提供端的版本号列表
        List<String> allNoFilterValueList = null;
        // 提供端规则未作任何定义
        boolean providerConditionDefined = false;
        for (VersionEntity versionEntity : versionEntityList) {
            String providerServiceName = versionEntity.getProviderServiceName();
            if (StringUtils.equalsIgnoreCase(providerServiceName, providerServiceId)) {
                providerConditionDefined = true;

                List<String> consumerVersionValueList = versionEntity.getConsumerVersionValueList();
                List<String> providerVersionValueList = versionEntity.getProviderVersionValueList();

                // 判断consumer-version-value值是否包含当前消费端的版本号
                // 如果consumerVersionValueList为空，表示消费端版本列表未指定，那么任意消费端版本可以访问指定版本提供端版本
                if (CollectionUtils.isNotEmpty(consumerVersionValueList)) {
                    if (consumerVersionValueList.contains(consumerServiceVersion)) {
                        if (allNoFilterValueList == null) {
                            allNoFilterValueList = new ArrayList<String>();
                        }
                        if (CollectionUtils.isNotEmpty(providerVersionValueList)) {
                            allNoFilterValueList.addAll(providerVersionValueList);
                        }
                    } // 这里的条件，在每一次循环都不满足，会让allNoFilterValueList为null，意味着定义的版本关系都不匹配
                } else {
                    if (allNoFilterValueList == null) {
                        allNoFilterValueList = new ArrayList<String>();
                    }
                    if (CollectionUtils.isNotEmpty(providerVersionValueList)) {
                        allNoFilterValueList.addAll(providerVersionValueList);
                    }
                }
            }
        }

        if (allNoFilterValueList != null) {
            // 当allNoFilterValueList为空列表，意味着版本对应关系未做任何定义（即所有的providerVersionValueList为空），不需要执行过滤，直接返回
            if (allNoFilterValueList.isEmpty()) {
                return;
            } else {
                Iterator<? extends Server> iterator = servers.iterator();
                while (iterator.hasNext()) {
                    Server server = iterator.next();
                    String serverVersion = pluginAdapter.getServerVersion(server);
                    if (!allNoFilterValueList.contains(serverVersion)) {
                        iterator.remove();
                    }
                }
            }
        } else {
            if (providerConditionDefined) {
                // 当allNoFilterValueList为null, 意味着定义的版本关系都不匹配，直接清空所有实例
                servers.clear();
            }
        }
    }

    @Override
    public int getOrder() {
        // After host filter
        return HIGHEST_PRECEDENCE + 1;
    }
}