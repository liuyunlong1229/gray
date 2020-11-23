package com.rainbow.gray.framework.entity;



import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MapWeightEntity implements Serializable {
    private static final long serialVersionUID = 3356648245119125011L;

    private Map<String, Integer> weightMap = new LinkedHashMap<String, Integer>();

    public Map<String, Integer> getWeightMap() {
        return weightMap;
    }

    public void setWeightMap(Map<String, Integer> weightMap) {
        this.weightMap = weightMap;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}