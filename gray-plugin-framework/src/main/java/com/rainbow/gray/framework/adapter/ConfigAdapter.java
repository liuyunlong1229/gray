package com.rainbow.gray.framework.adapter;

import org.springframework.beans.factory.annotation.Autowired;

import com.rainbow.gray.framework.event.PluginEventWapper;
import com.rainbow.gray.framework.event.RuleClearedEvent;
import com.rainbow.gray.framework.event.RuleUpdatedEvent;
import com.rainbow.gray.framework.loader.RemoteConfigLoader;

public abstract class ConfigAdapter extends RemoteConfigLoader {
    @Autowired
    private PluginEventWapper pluginEventWapper;

    public void fireRuleUpdated(RuleUpdatedEvent ruleUpdatedEvent, boolean async) {
        pluginEventWapper.fireRuleUpdated(ruleUpdatedEvent, async);
    }

    public void fireRuleCleared(RuleClearedEvent ruleClearedEvent, boolean async) {
        pluginEventWapper.fireRuleCleared(ruleClearedEvent, async);
    }
}