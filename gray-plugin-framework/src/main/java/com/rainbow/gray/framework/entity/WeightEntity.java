package com.rainbow.gray.framework.entity;

public class WeightEntity extends MapWeightEntity {
    private static final long serialVersionUID = 4242297554671632704L;

    private String consumerServiceName;
    private String providerServiceName;
    private WeightType type;

    public String getConsumerServiceName() {
        return consumerServiceName;
    }

    public void setConsumerServiceName(String consumerServiceName) {
        this.consumerServiceName = consumerServiceName;
    }

    public String getProviderServiceName() {
        return providerServiceName;
    }

    public void setProviderServiceName(String providerServiceName) {
        this.providerServiceName = providerServiceName;
    }

    public WeightType getType() {
        return type;
    }

    public void setType(WeightType type) {
        this.type = type;
    }
}