package com.rainbow.gray.framework.entity;

import java.io.Serializable;


public class DiscoveryEntity implements Serializable {
    private static final long serialVersionUID = -7417362859952278987L;

    private VersionFilterEntity versionFilterEntity;
    private RegionFilterEntity regionFilterEntity;
    private WeightFilterEntity weightFilterEntity;

    private BlacklistEntity blacklistEntity;
    
	public VersionFilterEntity getVersionFilterEntity() {
		return versionFilterEntity;
	}

	public void setVersionFilterEntity(VersionFilterEntity versionFilterEntity) {
		this.versionFilterEntity = versionFilterEntity;
	}

	public RegionFilterEntity getRegionFilterEntity() {
		return regionFilterEntity;
	}

	public void setRegionFilterEntity(RegionFilterEntity regionFilterEntity) {
		this.regionFilterEntity = regionFilterEntity;
	}

	public WeightFilterEntity getWeightFilterEntity() {
		return weightFilterEntity;
	}

	public void setWeightFilterEntity(WeightFilterEntity weightFilterEntity) {
		this.weightFilterEntity = weightFilterEntity;
	}

	public BlacklistEntity getBlacklistEntity() {
		return blacklistEntity;
	}

	public void setBlacklistEntity(BlacklistEntity blacklistEntity) {
		this.blacklistEntity = blacklistEntity;
	}
    
}