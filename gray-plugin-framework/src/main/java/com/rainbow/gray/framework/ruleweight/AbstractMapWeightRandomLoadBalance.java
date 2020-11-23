package com.rainbow.gray.framework.ruleweight;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.netflix.loadbalancer.Server;

public abstract class AbstractMapWeightRandomLoadBalance<T> implements WeightRandomLoadBalance<T> {
    @Override
    public Server choose(List<Server> serverList, T t) {
        if (CollectionUtils.isEmpty(serverList)) {
            return null;
        }

        List<Pair<Server, Integer>> weightPairList = new ArrayList<Pair<Server, Integer>>();
        for (Server server : serverList) {
            int weight = getWeight(server, t);
            weightPairList.add(new ImmutablePair<Server, Integer>(server, weight));
        }

        MapWeightRandom<Server, Integer> weightRandom = new MapWeightRandom<Server, Integer>(weightPairList);

        return weightRandom.random();
    }
}