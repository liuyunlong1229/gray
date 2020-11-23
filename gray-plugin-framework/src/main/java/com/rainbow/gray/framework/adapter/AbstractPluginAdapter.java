package com.rainbow.gray.framework.adapter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.serviceregistry.Registration;

import com.netflix.loadbalancer.Server;
import com.rainbow.gray.framework.cache.PluginCache;
import com.rainbow.gray.framework.cache.RuleCache;
import com.rainbow.gray.framework.constant.GrayConstant;
import com.rainbow.gray.framework.entity.RuleEntity;
import com.rainbow.gray.framework.entity.RuleEntityWrapper;
import com.rainbow.gray.framework.exception.GrayException;

public abstract class AbstractPluginAdapter implements PluginAdapter {
    @Autowired
    protected Registration registration;
    
    @Autowired
    protected PluginCache pluginCache;

    @Override
    public String getAppChannel() {
        return getMetadata().get(GrayConstant.APP_CHANNEL);
    }

    @Override
    public String getAppVer() {
        return getMetadata().get(GrayConstant.APP_VER);
    }

    @Autowired
    protected RuleCache ruleCache;

    @Value("${" + GrayConstant.SPRING_APPLICATION_GROUP_KEY + ":" + GrayConstant.GROUP + "}")
    private String groupKey;

    @Value("${" + GrayConstant.SPRING_APPLICATION_TYPE + ":" + GrayConstant.UNKNOWN + "}")
    private String applicationType;

    protected Map<String, String> emptyMetadata = new HashMap<String, String>();

    @Override
    public String getPlugin() {
        return getMetadata().get(GrayConstant.SPRING_APPLICATION_DISCOVERY_PLUGIN);
    }

    @Override
    public String getGroupKey() {
        return groupKey;
    }

    @Override
    public String getGroup() {
        String groupKey = getGroupKey();

        String group = getGroup(groupKey);
        if (StringUtils.isEmpty(group)) {
            group = GrayConstant.DEFAULT;
        }

        return group;
    }

    protected String getGroup(String groupKey) {
        return getMetadata().get(groupKey);
    }

    @Override
    public String getServiceType() {
        return applicationType;
    }

    @Override
    public String getServiceId() {
        return registration.getServiceId().toLowerCase();
    }

    @Override
    public String getServiceUUId() {
        return getMetadata().get(GrayConstant.SPRING_APPLICATION_UUID);
    }

    @Override
    public String getHost() {
        return registration.getHost();
    }

    @Override
    public int getPort() {
        return registration.getPort();
    }

    @Override
    public Map<String, String> getMetadata() {
        return registration.getMetadata();
    }

    @Override
    public String getVersion() {
        return getLocalVersion();
    }

    @Override
    public String getLocalVersion() {
        String version = getMetadata().get(GrayConstant.VERSION);
        if (StringUtils.isEmpty(version)) {
            version = GrayConstant.DEFAULT;
        }

        return version;
    }

    @Override
    public RuleEntity getRule() {
        RuleEntity dynamicRuleEntity = getDynamicRule();
        if (dynamicRuleEntity != null) {
            return dynamicRuleEntity;
        }

        return getLocalRule();
    }

    @Override
    public RuleEntity getLocalRule() {
        return ruleCache.get(GrayConstant.RULE);
    }

    @Override
    public void setLocalRule(RuleEntity ruleEntity) {
        ruleCache.put(GrayConstant.RULE, ruleEntity);
    }
    
    @Override
    public String getDynamicVersion() {
        return pluginCache.get(GrayConstant.DYNAMIC_VERSION);
    }

    @Override
    public void setDynamicVersion(String version) {
        pluginCache.put(GrayConstant.DYNAMIC_VERSION, version);
    }

    @Override
    public void clearDynamicVersion() {
        pluginCache.clear(GrayConstant.DYNAMIC_VERSION);
    }

    @Override
    public RuleEntity getDynamicRule() {
        return ruleCache.get(GrayConstant.DYNAMIC_RULE);
    }

    // 从动态全局规则和动态局部规则缓存组装出最终的动态规则
    private void assembleDynamicRule() {
        RuleEntity dynamicPartialRule = getDynamicPartialRule();
        RuleEntity dynamicGlobalRule = getDynamicGlobalRule();

        RuleEntity dynamicRule = RuleEntityWrapper.assemble(dynamicPartialRule, dynamicGlobalRule);
        ruleCache.put(GrayConstant.DYNAMIC_RULE, dynamicRule);
    }

    @Override
    public RuleEntity getDynamicPartialRule() {
        return ruleCache.get(GrayConstant.DYNAMIC_PARTIAL_RULE);
    }

    @Override
    public void setDynamicPartialRule(RuleEntity ruleEntity) {
        ruleCache.put(GrayConstant.DYNAMIC_PARTIAL_RULE, ruleEntity);

        assembleDynamicRule();
    }

    @Override
    public void clearDynamicPartialRule() {
        ruleCache.clear(GrayConstant.DYNAMIC_PARTIAL_RULE);

        assembleDynamicRule();
    }

    @Override
    public RuleEntity getDynamicGlobalRule() {
        return ruleCache.get(GrayConstant.DYNAMIC_GLOBAL_RULE);
    }

    @Override
    public void setDynamicGlobalRule(RuleEntity ruleEntity) {
        ruleCache.put(GrayConstant.DYNAMIC_GLOBAL_RULE, ruleEntity);

        assembleDynamicRule();
    }

    @Override
    public void clearDynamicGlobalRule() {
        ruleCache.clear(GrayConstant.DYNAMIC_GLOBAL_RULE);

        assembleDynamicRule();
    }

    @Override
    public String getRegion() {
        String region = getMetadata().get(GrayConstant.REGION);
        if (StringUtils.isEmpty(region)) {
            region = GrayConstant.DEFAULT;
        }

        return region;
    }

    @Override
    public String getEnvironment() {
        String environment = getMetadata().get(GrayConstant.ENVIRONMENT);
        if (StringUtils.isEmpty(environment)) {
            environment = GrayConstant.DEFAULT;
        }

        return environment;
    }

    @Override
    public String getZone() {
        String zone = getMetadata().get(GrayConstant.ZONE);
        if (StringUtils.isEmpty(zone)) {
            zone = GrayConstant.DEFAULT;
        }

        return zone;
    }

    @Override
    public String getContextPath() {
        return getMetadata().get(GrayConstant.SPRING_APPLICATION_CONTEXT_PATH);
    }

    @Override
    public String getServerPlugin(Server server) {
        return getServerMetadata(server).get(GrayConstant.SPRING_APPLICATION_DISCOVERY_PLUGIN);
    }

    @Override
    public String getServerGroupKey(Server server) {
        String groupKey = getServerMetadata(server).get(GrayConstant.SPRING_APPLICATION_GROUP_KEY);

        if (StringUtils.isEmpty(groupKey)) {
            groupKey = GrayConstant.GROUP;
        }

        return groupKey;
    }

    @Override
    public String getServerGroup(Server server) {
        String serverGroupKey = getServerGroupKey(server);

        String serverGroup = getServerMetadata(server).get(serverGroupKey);
        if (StringUtils.isEmpty(serverGroup)) {
            serverGroup = GrayConstant.DEFAULT;
        }

        return serverGroup;
    }

    @Override
    public String getServerServiceType(Server server) {
        return getServerMetadata(server).get(GrayConstant.SPRING_APPLICATION_TYPE);
    }

    @Override
    public String getServerServiceId(Server server) {
        String serviceId = getServerMetadata(server).get(GrayConstant.SPRING_APPLICATION_NAME);
        if (StringUtils.isEmpty(serviceId)) {
            serviceId = server.getMetaInfo().getAppName();
        }

        if (StringUtils.isEmpty(serviceId)) {
            throw new GrayException("Server ServiceId is null");
        }

        return serviceId.toLowerCase();
    }

    @Override
    public String getServerServiceUUId(Server server) {
        return getServerMetadata(server).get(GrayConstant.SPRING_APPLICATION_UUID);
    }

    @Override
    public String getServerVersion(Server server) {
        String serverVersion = getServerMetadata(server).get(GrayConstant.VERSION);
        if (StringUtils.isEmpty(serverVersion)) {
            serverVersion = GrayConstant.DEFAULT;
        }

        return serverVersion;
    }

    @Override
    public String getServerRegion(Server server) {
        String serverRegion = getServerMetadata(server).get(GrayConstant.REGION);
        if (StringUtils.isEmpty(serverRegion)) {
            serverRegion = GrayConstant.DEFAULT;
        }

        return serverRegion;
    }

    @Override
    public String getServerEnvironment(Server server) {
        String serverEnvironment = getServerMetadata(server).get(GrayConstant.ENVIRONMENT);
        if (StringUtils.isEmpty(serverEnvironment)) {
            serverEnvironment = GrayConstant.DEFAULT;
        }

        return serverEnvironment;
    }

    @Override
    public String getServerZone(Server server) {
        String serverZone = getServerMetadata(server).get(GrayConstant.ZONE);
        if (StringUtils.isEmpty(serverZone)) {
            serverZone = GrayConstant.DEFAULT;
        }

        return serverZone;
    }

    @Override
    public String getServerContextPath(Server server) {
        return getServerMetadata(server).get(GrayConstant.SPRING_APPLICATION_CONTEXT_PATH);
    }

    @Override
    public Map<String, String> getInstanceMetadata(ServiceInstance instance) {
        return instance.getMetadata();
    }

    @Override
    public String getInstancePlugin(ServiceInstance instance) {
        return getInstanceMetadata(instance).get(GrayConstant.SPRING_APPLICATION_DISCOVERY_PLUGIN);
    }

    @Override
    public String getInstanceGroupKey(ServiceInstance instance) {
        String groupKey = getInstanceMetadata(instance).get(GrayConstant.SPRING_APPLICATION_GROUP_KEY);

        if (StringUtils.isEmpty(groupKey)) {
            groupKey = GrayConstant.GROUP;
        }

        return groupKey;
    }

    @Override
    public String getInstanceGroup(ServiceInstance instance) {
        String instanceGroupKey = getInstanceGroupKey(instance);

        String instanceGroup = getInstanceMetadata(instance).get(instanceGroupKey);
        if (StringUtils.isEmpty(instanceGroup)) {
            instanceGroup = GrayConstant.DEFAULT;
        }

        return instanceGroup;
    }

    @Override
    public String getInstanceServiceType(ServiceInstance instance) {
        return getInstanceMetadata(instance).get(GrayConstant.SPRING_APPLICATION_TYPE);
    }

    @Override
    public String getInstanceServiceId(ServiceInstance instance) {
        return instance.getServiceId().toLowerCase();
    }

    @Override
    public String getInstanceServiceUUId(ServiceInstance instance) {
        return getInstanceMetadata(instance).get(GrayConstant.SPRING_APPLICATION_UUID);
    }

    @Override
    public String getInstanceVersion(ServiceInstance instance) {
        String instanceVersion = getInstanceMetadata(instance).get(GrayConstant.VERSION);
        if (StringUtils.isEmpty(instanceVersion)) {
            instanceVersion = GrayConstant.DEFAULT;
        }

        return instanceVersion;
    }

    @Override
    public String getInstanceRegion(ServiceInstance instance) {
        String instanceRegion = getInstanceMetadata(instance).get(GrayConstant.REGION);
        if (StringUtils.isEmpty(instanceRegion)) {
            instanceRegion = GrayConstant.DEFAULT;
        }

        return instanceRegion;
    }

    @Override
    public String getInstanceEnvironment(ServiceInstance instance) {
        String instanceEnvironment = getInstanceMetadata(instance).get(GrayConstant.ENVIRONMENT);
        if (StringUtils.isEmpty(instanceEnvironment)) {
            instanceEnvironment = GrayConstant.DEFAULT;
        }

        return instanceEnvironment;
    }

    @Override
    public String getInstanceZone(ServiceInstance instance) {
        String instanceZone = getInstanceMetadata(instance).get(GrayConstant.ZONE);
        if (StringUtils.isEmpty(instanceZone)) {
            instanceZone = GrayConstant.DEFAULT;
        }

        return instanceZone;
    }

    @Override
    public String getInstanceContextPath(ServiceInstance instance) {
        return getInstanceMetadata(instance).get(GrayConstant.SPRING_APPLICATION_CONTEXT_PATH);
    }

    @Override
    public String getPluginInfo(String previousPluginInfo) {
        String plugin = getPlugin();
        String serviceId = getServiceId();
        String host = getHost();
        int port = getPort();
        String version = getVersion();
        String region = getRegion();
        String environment = getEnvironment();
        String zone = getZone();
        String group = getGroup();
        String appVer=getAppVer();
        String appChannel=getAppChannel();
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(previousPluginInfo)) {
            stringBuilder.append(previousPluginInfo + " -> ");
        }

        stringBuilder.append("[ID=" + serviceId + "]");
        stringBuilder.append("[P=" + plugin + "]");
        stringBuilder.append("[H=" + host + ":" + port + "]");
        if (StringUtils.isNotEmpty(version)) {
            stringBuilder.append("[V=" + version + "]");
        }
        if (StringUtils.isNotEmpty(region)) {
            stringBuilder.append("[R=" + region + "]");
        }
        if (StringUtils.isNotEmpty(environment)) {
            stringBuilder.append("[E=" + environment + "]");
        }
        if (StringUtils.isNotEmpty(zone)) {
            stringBuilder.append("[Z=" + zone + "]");
        }
        if (StringUtils.isNotEmpty(group)) {
            stringBuilder.append("[G=" + group + "]");
        }
        stringBuilder.append("[appVer=" + appVer + "]");
        stringBuilder.append("[appChannel=" + appChannel + "]");

//        if (pluginContextHolder != null) {
//            String traceId = pluginContextHolder.getTraceId();
//            if (StringUtils.isNotEmpty(traceId)) {
//                stringBuilder.append("[TID=" + traceId + "]");
//            }
//
//            String spanId = pluginContextHolder.getSpanId();
//            if (StringUtils.isNotEmpty(spanId)) {
//                stringBuilder.append("[SID=" + spanId + "]");
//            }
//        }

        return stringBuilder.toString();
    }
}