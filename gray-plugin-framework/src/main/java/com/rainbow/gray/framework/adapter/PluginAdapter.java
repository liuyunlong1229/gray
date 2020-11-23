package com.rainbow.gray.framework.adapter;

import java.util.Map;

import org.springframework.cloud.client.ServiceInstance;

import com.netflix.loadbalancer.Server;
import com.rainbow.gray.framework.entity.RuleEntity;

public interface PluginAdapter {
    String getPlugin();

    String getGroupKey();

    String getGroup();

    String getServiceType();

    String getServiceId();

    String getServiceUUId();

    String getHost();

    int getPort();

    Map<String, String> getMetadata();

    String getVersion();

    String getLocalVersion();

    RuleEntity getRule();

    RuleEntity getLocalRule();

    void setLocalRule(RuleEntity ruleEntity);
    
    String getDynamicVersion();

    void setDynamicVersion(String version);

    void clearDynamicVersion();

    RuleEntity getDynamicRule();

    RuleEntity getDynamicPartialRule();

    void setDynamicPartialRule(RuleEntity ruleEntity);

    void clearDynamicPartialRule();

    RuleEntity getDynamicGlobalRule();

    void setDynamicGlobalRule(RuleEntity ruleEntity);

    void clearDynamicGlobalRule();

    String getRegion();

    String getEnvironment();

    String getZone();

    String getContextPath();

    Map<String, String> getServerMetadata(Server server);

    String getServerPlugin(Server server);

    String getServerGroupKey(Server server);

    String getServerGroup(Server server);

    String getServerServiceType(Server server);

    String getServerServiceId(Server server);

    String getServerServiceUUId(Server server);

    String getServerVersion(Server server);

    String getServerRegion(Server server);

    String getServerEnvironment(Server server);

    String getServerZone(Server server);

    String getServerContextPath(Server server);

    Map<String, String> getInstanceMetadata(ServiceInstance instance);

    String getInstancePlugin(ServiceInstance instance);

    String getInstanceGroupKey(ServiceInstance instance);

    String getInstanceGroup(ServiceInstance instance);

    String getInstanceServiceType(ServiceInstance instance);

    String getInstanceServiceId(ServiceInstance instance);

    String getInstanceServiceUUId(ServiceInstance instance);

    String getInstanceVersion(ServiceInstance instance);

    String getInstanceRegion(ServiceInstance instance);

    String getInstanceEnvironment(ServiceInstance instance);

    String getInstanceZone(ServiceInstance instance);

    String getInstanceContextPath(ServiceInstance instance);

    String getPluginInfo(String previousPluginInfo);

    String getAppChannel();

    String getAppVer();
}