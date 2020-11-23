package com.rainbow.gray.framework.entity;



import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RegionFilterEntity implements Serializable {
    private static final long serialVersionUID = 5574079766640689952L;

    private Map<String, List<RegionEntity>> regionEntityMap = new LinkedHashMap<String, List<RegionEntity>>();

    public Map<String, List<RegionEntity>> getRegionEntityMap() {
        return regionEntityMap;
    }

    public void setRegionEntityMap(Map<String, List<RegionEntity>> regionEntityMap) {
        this.regionEntityMap = regionEntityMap;
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