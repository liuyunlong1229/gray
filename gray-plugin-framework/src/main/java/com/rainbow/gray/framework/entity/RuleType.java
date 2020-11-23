package com.rainbow.gray.framework.entity;

import com.rainbow.gray.framework.constant.GrayConstant;

public enum RuleType {
    DYNAMIC_GLOBAL_RULE(GrayConstant.DYNAMIC_GLOBAL_RULE, GrayConstant.DYNAMIC_GLOBAL_RULE), 
    DYNAMIC_PARTIAL_RULE(GrayConstant.DYNAMIC_PARTIAL_RULE, GrayConstant.DYNAMIC_PARTIAL_RULE);

    private String value;
    private String description;

    private RuleType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static RuleType fromString(String value) {
        for (RuleType type : RuleType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new IllegalArgumentException("No matched type with value=" + value);
    }

    @Override
    public String toString() {
        return value;
    }
}