package com.rainbow.gray.framework.example.service.rest;



import org.springframework.beans.factory.annotation.Autowired;

import com.rainbow.gray.framework.adapter.PluginAdapter;

public class AbstractRestImpl {
    @Autowired
    private PluginAdapter pluginAdapter;

    public String doRest(String value) {
        return pluginAdapter.getPluginInfo(value);
    }
}