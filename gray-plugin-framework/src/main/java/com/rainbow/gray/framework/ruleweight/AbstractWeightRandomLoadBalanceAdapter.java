package com.rainbow.gray.framework.ruleweight;

import java.util.List;

import com.netflix.loadbalancer.Server;
import com.rainbow.gray.framework.adapter.PluginAdapter;

public abstract class AbstractWeightRandomLoadBalanceAdapter<T> {
    protected PluginAdapter pluginAdapter;
    //protected PluginContextHolder pluginContextHolder;

    public AbstractWeightRandomLoadBalanceAdapter(PluginAdapter pluginAdapter) {
//        this(pluginAdapter, null);
    	this.pluginAdapter = pluginAdapter;
    }

//    public AbstractWeightRandomLoadBalanceAdapter(PluginAdapter pluginAdapter, PluginContextHolder pluginContextHolder) {
//        this.pluginAdapter = pluginAdapter;
//        //this.pluginContextHolder = pluginContextHolder;
//    }

    public boolean checkWeight(List<Server> serverList, T t) {
        for (Server server : serverList) {
            int weight = getWeight(server, t);
            if (weight < 0) {
                return false;
            }
        }

        return true;
    }

    public abstract T getT();

    public abstract int getWeight(Server server, T t);
}