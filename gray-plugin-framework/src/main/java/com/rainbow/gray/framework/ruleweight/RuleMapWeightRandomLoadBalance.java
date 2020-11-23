package com.rainbow.gray.framework.ruleweight;



import java.util.List;

import com.netflix.loadbalancer.Server;
import com.rainbow.gray.framework.adapter.PluginAdapter;
import com.rainbow.gray.framework.entity.WeightFilterEntity;

public class RuleMapWeightRandomLoadBalance extends AbstractMapWeightRandomLoadBalance<WeightFilterEntity> {
    private RuleWeightRandomLoadBalanceAdapter ruleWeightRandomLoadBalanceAdapter;

    public RuleMapWeightRandomLoadBalance(PluginAdapter pluginAdapter) {
        ruleWeightRandomLoadBalanceAdapter = new RuleWeightRandomLoadBalanceAdapter(pluginAdapter);
    }

    @Override
    public WeightFilterEntity getT() {
        return ruleWeightRandomLoadBalanceAdapter.getT();
    }

    @Override
    public int getWeight(Server server, WeightFilterEntity weightFilterEntity) {
        return ruleWeightRandomLoadBalanceAdapter.getWeight(server, weightFilterEntity);
    }

    @Override
    public boolean checkWeight(List<Server> serverList, WeightFilterEntity weightFilterEntity) {
        return ruleWeightRandomLoadBalanceAdapter.checkWeight(serverList, weightFilterEntity);
    }
}