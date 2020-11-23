package com.rainbow.gray.framework.entity;

import java.util.List;

/**
 * @author yunlong.liu
 * @date 2020-11-23 16:05:53
 */

public class ServicelistEntity {

    private String serviceId;

    private PodLabel podLabel;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public PodLabel getPodLabel() {
        return podLabel;
    }

    public void setPodLabel(PodLabel podLabel) {
        this.podLabel = podLabel;
    }


    public static class PodLabel{

        private String appChannel;
        private String appVer;

        public String getAppChannel() {
            return appChannel;
        }

        public void setAppChannel(String appChannel) {
            this.appChannel = appChannel;
        }

        public String getAppVer() {
            return appVer;
        }

        public void setAppVer(String appVer) {
            this.appVer = appVer;
        }
    }
}
