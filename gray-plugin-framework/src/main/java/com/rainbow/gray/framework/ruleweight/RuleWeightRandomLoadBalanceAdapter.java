package com.rainbow.gray.framework.ruleweight;



import com.netflix.loadbalancer.Server;
import com.rainbow.gray.framework.adapter.PluginAdapter;
import com.rainbow.gray.framework.entity.DiscoveryEntity;
import com.rainbow.gray.framework.entity.RuleEntity;
import com.rainbow.gray.framework.entity.WeightEntityWrapper;
import com.rainbow.gray.framework.entity.WeightFilterEntity;

public class RuleWeightRandomLoadBalanceAdapter extends AbstractWeightRandomLoadBalanceAdapter<WeightFilterEntity> {
    public RuleWeightRandomLoadBalanceAdapter(PluginAdapter pluginAdapter) {
        super(pluginAdapter);
    }

    @Override
    public WeightFilterEntity getT() {
        RuleEntity ruleEntity = pluginAdapter.getRule();
        if (ruleEntity == null) {
            return null;
        }

        DiscoveryEntity discoveryEntity = ruleEntity.getDiscoveryEntity();
        if (discoveryEntity == null) {
            return null;
        }

        WeightFilterEntity weightFilterEntity = discoveryEntity.getWeightFilterEntity();

        return weightFilterEntity;
    }

    @Override
    public int getWeight(Server server, WeightFilterEntity weightFilterEntity) {
        String providerServiceId = pluginAdapter.getServerServiceId(server);
        String providerVersion = pluginAdapter.getServerVersion(server);
        String providerRegion = pluginAdapter.getServerRegion(server);
        String serviceId = pluginAdapter.getServiceId();

        return WeightEntityWrapper.getWeight(weightFilterEntity, providerServiceId, providerVersion, providerRegion, serviceId);
    }
}