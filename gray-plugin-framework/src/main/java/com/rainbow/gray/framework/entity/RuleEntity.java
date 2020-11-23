package com.rainbow.gray.framework.entity;



import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RuleEntity implements Serializable {
    private static final long serialVersionUID = 7079024435084528751L;

    private DiscoveryEntity discoveryEntity;
    
    private String content;

    public DiscoveryEntity getDiscoveryEntity() {
		return discoveryEntity;
	}

	public void setDiscoveryEntity(DiscoveryEntity discoveryEntity) {
		this.discoveryEntity = discoveryEntity;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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