package com.rainbow.gray.framework.entity;

import com.rainbow.gray.framework.constant.GrayConstant;

public enum WeightType {
    VERSION(GrayConstant.VERSION, GrayConstant.VERSION),
    REGION(GrayConstant.REGION, GrayConstant.REGION),
    ADDRESS(GrayConstant.ADDRESS, GrayConstant.ADDRESS);

    private String value;
    private String description;

    private WeightType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static WeightType fromString(String value) {
        for (WeightType type : WeightType.values()) {
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