package com.rainbow.gray.framework.adapter;

import java.util.Map;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.Server;

public class NacosAdapter extends AbstractPluginAdapter {
    @Override
    public Map<String, String> getServerMetadata(Server server) {
        if (server instanceof NacosServer) {
            NacosServer nacosServer = (NacosServer) server;

            return nacosServer.getMetadata();
        }

        return emptyMetadata;

    }
}