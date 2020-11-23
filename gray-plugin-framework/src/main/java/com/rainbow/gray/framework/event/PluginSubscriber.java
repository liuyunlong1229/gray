package com.rainbow.gray.framework.event;



import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.nepxion.eventbus.annotation.EventBus;
import com.rainbow.gray.framework.adapter.PluginAdapter;
import com.rainbow.gray.framework.config.PluginConfigParser;
import com.rainbow.gray.framework.context.PluginContextAware;
import com.rainbow.gray.framework.entity.RuleEntity;
import com.rainbow.gray.framework.entity.RuleType;
import com.rainbow.gray.framework.exception.GrayException;
import com.rainbow.gray.framework.ribbon.RibbonProcessor;

@EventBus
public class PluginSubscriber {
    private static final Logger LOG = LoggerFactory.getLogger(PluginSubscriber.class);

    @Autowired
    private PluginContextAware pluginContextAware;

    @Autowired
    private PluginAdapter pluginAdapter;

    @Autowired
    private PluginConfigParser pluginConfigParser;

    @Autowired
    private PluginEventWapper pluginEventWapper;

    @Autowired
    private RibbonProcessor ribbonProcessor;

    @Subscribe
    public void onRuleUpdated(RuleUpdatedEvent ruleUpdatedEvent) {
        Boolean discoveryControlEnabled = pluginContextAware.isDiscoveryControlEnabled();
        if (!discoveryControlEnabled) {
            LOG.info("Discovery control is disabled, ignore to subscribe");

            return;
        }

        LOG.info("Rule updating has been triggered");

        if (ruleUpdatedEvent == null) {
            throw new GrayException("RuleUpdatedEvent can't be null");
        }

        RuleType ruleType = ruleUpdatedEvent.getRuleType();
        String rule = ruleUpdatedEvent.getRule();
        try {
            RuleEntity ruleEntity = pluginConfigParser.parse(rule);
            switch (ruleType) {
                case DYNAMIC_GLOBAL_RULE:
                    pluginAdapter.setDynamicGlobalRule(ruleEntity);
                    break;
                case DYNAMIC_PARTIAL_RULE:
                    pluginAdapter.setDynamicPartialRule(ruleEntity);
                    break;
            }
        } catch (Exception e) {
            LOG.error("Parse rule xml failed", e);

            pluginEventWapper.fireRuleFailure(new RuleFailureEvent(ruleType, rule, e));

            throw e;
        }

        refreshLoadBalancer();
    }

    @Subscribe
    public void onRuleCleared(RuleClearedEvent ruleClearedEvent) {
        Boolean discoveryControlEnabled = pluginContextAware.isDiscoveryControlEnabled();
        if (!discoveryControlEnabled) {
            LOG.info("Discovery control is disabled, ignore to subscribe");

            return;
        }

        LOG.info("Rule clearing has been triggered");

        if (ruleClearedEvent == null) {
            throw new GrayException("RuleClearedEvent can't be null");
        }

        RuleType ruleType = ruleClearedEvent.getRuleType();
        switch (ruleType) {
            case DYNAMIC_GLOBAL_RULE:
                pluginAdapter.clearDynamicGlobalRule();
                break;
            case DYNAMIC_PARTIAL_RULE:
                pluginAdapter.clearDynamicPartialRule();
                break;
        }

        refreshLoadBalancer();
    }

    @Subscribe
    public void onVersionUpdated(VersionUpdatedEvent versionUpdatedEvent) {
        Boolean discoveryControlEnabled = pluginContextAware.isDiscoveryControlEnabled();
        if (!discoveryControlEnabled) {
            LOG.info("Discovery control is disabled, ignore to subscribe");

            return;
        }

        LOG.info("Version updating has been triggered");

        if (versionUpdatedEvent == null) {
            throw new GrayException("VersionUpdatedEvent can't be null");
        }

        String dynamicVersion = versionUpdatedEvent.getDynamicVersion();
        String localVersion = versionUpdatedEvent.getLocalVersion();

        if (StringUtils.isEmpty(localVersion)) {
            pluginAdapter.setDynamicVersion(dynamicVersion);

            refreshLoadBalancer();

            LOG.info("Version has been updated, new version is {}", dynamicVersion);
        } else {
            if (StringUtils.equals(pluginAdapter.getLocalVersion(), localVersion)) {
                pluginAdapter.setDynamicVersion(dynamicVersion);

                refreshLoadBalancer();

                LOG.info("Version has been updated, new version is {}", dynamicVersion);
            } else {
                throw new GrayException("Version updating will be ignored, because input localVersion=" + localVersion + ", current localVersion=" + pluginAdapter.getLocalVersion());
            }
        }
    }

    @Subscribe
    public void onVersionCleared(VersionClearedEvent versionClearedEvent) {
        Boolean discoveryControlEnabled = pluginContextAware.isDiscoveryControlEnabled();
        if (!discoveryControlEnabled) {
            LOG.info("Discovery control is disabled, ignore to subscribe");

            return;
        }

        LOG.info("Version clearing has been triggered");

        if (versionClearedEvent == null) {
            throw new GrayException("VersionClearedEvent can't be null");
        }

        String localVersion = versionClearedEvent.getLocalVersion();

        if (StringUtils.isEmpty(localVersion)) {
            pluginAdapter.clearDynamicVersion();

            refreshLoadBalancer();

            LOG.info("Version has been cleared");
        } else {
            if (StringUtils.equals(pluginAdapter.getLocalVersion(), localVersion)) {
                pluginAdapter.clearDynamicVersion();

                refreshLoadBalancer();

                LOG.info("Version has been cleared");
            } else {
                throw new GrayException("Version clearing will be ignored, because input localVersion=" + localVersion + ", current localVersion=" + pluginAdapter.getLocalVersion());
            }
        }
    }

    // 当规则或者版本更新后，强制刷新Ribbon缓存
    private void refreshLoadBalancer() {
        ribbonProcessor.refreshLoadBalancer();
    }
}