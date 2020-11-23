package com.rainbow.gray.framework.event;

import java.io.Serializable;

import com.rainbow.gray.framework.entity.RuleType;

public class RuleUpdatedEvent implements Serializable {
    private static final long serialVersionUID = 2315578803987663866L;

    private RuleType ruleType;
    private String rule;

    public RuleUpdatedEvent(RuleType ruleType, String rule) {
        this.ruleType = ruleType;
        this.rule = rule;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public String getRule() {
        return rule;
    }
}