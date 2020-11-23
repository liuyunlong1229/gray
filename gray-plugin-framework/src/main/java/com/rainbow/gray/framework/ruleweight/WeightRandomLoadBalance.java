package com.rainbow.gray.framework.ruleweight;



import java.util.List;

import com.netflix.loadbalancer.Server;

public interface WeightRandomLoadBalance<T> {
    T getT();

    int getWeight(Server server, T t);

    boolean checkWeight(List<Server> serverList, T t);

    Server choose(List<Server> serverList, T t);
}