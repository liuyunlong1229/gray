package com.rainbow.gray.framework.event;



import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.rainbow.gray.framework.exception.GrayException;

public class VersionUpdatedEvent implements Serializable {
    private static final long serialVersionUID = 7749946311426379329L;

    private String dynamicVersion;
    private String localVersion;

    public VersionUpdatedEvent(String dynamicVersion) {
        this(dynamicVersion, null);
    }

    public VersionUpdatedEvent(String dynamicVersion, String localVersion) {
        if (StringUtils.isNotEmpty(dynamicVersion)) {
            this.dynamicVersion = dynamicVersion.trim();
        }

        if (StringUtils.isEmpty(this.dynamicVersion)) {
            throw new GrayException("Dynamic version can't be null or empty while updating");
        }

        if (StringUtils.isNotEmpty(localVersion)) {
            this.localVersion = localVersion.trim();
        }
    }

    public String getDynamicVersion() {
        return dynamicVersion;
    }

    public String getLocalVersion() {
        return localVersion;
    }
}