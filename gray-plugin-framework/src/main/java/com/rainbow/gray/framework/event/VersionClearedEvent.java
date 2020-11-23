package com.rainbow.gray.framework.event;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class VersionClearedEvent implements Serializable {
    private static final long serialVersionUID = 5079797986381461496L;

    private String localVersion;

    public VersionClearedEvent() {
        this(null);
    }

    public VersionClearedEvent(String localVersion) {
        if (StringUtils.isNotEmpty(localVersion)) {
            this.localVersion = localVersion.trim();
        }
    }

    public String getLocalVersion() {
        return localVersion;
    }
}