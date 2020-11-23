package com.rainbow.gray.framework.context;



public interface PluginContextHolder {
    String getContext(String name);

    String getContextRouteVersion();

    String getContextRouteRegion();

    String getContextRouteEnvironment();

    String getContextRouteAddress();

    String getContextRouteVersionWeight();

    String getContextRouteRegionWeight();

    String getContextRouteIdBlacklist();

    String getContextRouteAddressBlacklist();

    String getTraceId();

    String getSpanId();
}