package com.rainbow.gray.framework.event;

import java.io.Serializable;

import com.rainbow.gray.framework.entity.RuleType;

public class RuleFailureEvent implements Serializable {
    private static final long serialVersionUID = 954041724496099958L;

    private RuleType ruleType;
    private String rule;
    private Exception exception;

    public RuleFailureEvent(RuleType ruleType, String rule, Exception exception) {
        this.ruleType = ruleType;
        this.rule = rule;
        this.exception = exception;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public String getRule() {
        return rule;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}