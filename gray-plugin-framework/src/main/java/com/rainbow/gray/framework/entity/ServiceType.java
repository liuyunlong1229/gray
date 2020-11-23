package com.rainbow.gray.framework.entity;

import com.rainbow.gray.framework.constant.GrayConstant;

public enum ServiceType {
    SERVICE(GrayConstant.SERVICE, GrayConstant.SERVICE), 
    GATEWAY(GrayConstant.GATEWAY, GrayConstant.GATEWAY), 
    CONSOLE(GrayConstant.CONSOLE, GrayConstant.CONSOLE), 
    TEST(GrayConstant.TEST, GrayConstant.TEST);

    private String value;
    private String description;

    private ServiceType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static ServiceType fromString(String value) {
        for (ServiceType type : ServiceType.values()) {
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