package com.rainbow.gray.framework.decorator;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.netflix.loadbalancer.PredicateBasedRule;
import com.netflix.loadbalancer.Server;
import com.rainbow.gray.framework.adapter.PluginAdapter;
import com.rainbow.gray.framework.constant.GrayConstant;
import com.rainbow.gray.framework.entity.WeightFilterEntity;
import com.rainbow.gray.framework.ruleweight.RuleMapWeightRandomLoadBalance;

public abstract class PredicateBasedRuleDecorator extends PredicateBasedRule {
    private static final Logger LOG = LoggerFactory.getLogger(PredicateBasedRuleDecorator.class);

    @Value("${" + GrayConstant.SPRING_APPLICATION_NO_SERVERS_RETRY_ENABLED + ":false}")
    private Boolean retryEnabled;

    @Value("${" + GrayConstant.SPRING_APPLICATION_NO_SERVERS_RETRY_TIMES + ":5}")
    private Integer retryTimes;

    @Value("${" + GrayConstant.SPRING_APPLICATION_NO_SERVERS_RETRY_AWAIT_TIME + ":2000}")
    private Integer retryAwaitTime;

    @Autowired
    private PluginAdapter pluginAdapter;

    private RuleMapWeightRandomLoadBalance ruleMapWeightRandomLoadBalance;

    @PostConstruct
    private void initialize() {
        ruleMapWeightRandomLoadBalance = new RuleMapWeightRandomLoadBalance(pluginAdapter);
    }

    // 必须执行getEligibleServers，否则叠加执行权重规则和版本区域策略会失效
    private List<Server> getServerList(Object key) {
        return getPredicate().getEligibleServers(getLoadBalancer().getAllServers(), key);
    }

    private List<Server> getRetryableServerList(Object key) {
        List<Server> serverList = getServerList(key);
        for (int i = 0; i < retryTimes; i++) {
            if (CollectionUtils.isNotEmpty(serverList)) {
                break;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(retryAwaitTime);
            } catch (InterruptedException e) {

            }

            LOG.info("Retry to get server list for {} times...", i + 1);

            serverList = getServerList(key);
        }

        return serverList;
    }

    @Override
    public Server choose(Object key) {
        
        WeightFilterEntity ruleWeightFilterEntity = ruleMapWeightRandomLoadBalance.getT();
        if (ruleWeightFilterEntity != null && ruleWeightFilterEntity.hasWeight()) {
            List<Server> serverList = retryEnabled ? getRetryableServerList(key) : getServerList(key);
            boolean isWeightChecked = ruleMapWeightRandomLoadBalance.checkWeight(serverList, ruleWeightFilterEntity);
            if (isWeightChecked) {
                try {
                    return ruleMapWeightRandomLoadBalance.choose(serverList, ruleWeightFilterEntity);
                } catch (Exception e) {
                    // LOG.error("Execute rule weight-random-loadbalance failed, it will use default loadbalance", e);

                    return super.choose(key);
                }
            } else {
                return super.choose(key);
            }
        }

        return super.choose(key);
    }
}