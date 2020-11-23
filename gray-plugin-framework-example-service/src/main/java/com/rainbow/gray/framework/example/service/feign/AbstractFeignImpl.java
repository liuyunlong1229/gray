package com.rainbow.gray.framework.example.service.feign;

import org.springframework.beans.factory.annotation.Autowired;

import com.rainbow.gray.framework.adapter.PluginAdapter;

public class AbstractFeignImpl {
    @Autowired
    private PluginAdapter pluginAdapter;

    public String doInvoke(String value) {
        return pluginAdapter.getPluginInfo(value);
    }
}