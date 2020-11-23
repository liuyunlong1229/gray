package com.rainbow.gray.framework.event;

import java.io.Serializable;

import com.rainbow.gray.framework.entity.RuleType;

public class RuleClearedEvent implements Serializable {
    private static final long serialVersionUID = -4942710381954711909L;

    private RuleType ruleType;

    public RuleClearedEvent(RuleType ruleType) {
        this.ruleType = ruleType;
    }

    public RuleType getRuleType() {
        return ruleType;
    }
}