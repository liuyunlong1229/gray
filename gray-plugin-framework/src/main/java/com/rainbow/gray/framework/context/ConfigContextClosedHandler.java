package com.rainbow.gray.framework.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import com.rainbow.gray.framework.loader.RemoteConfigLoader;

public class ConfigContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
    @Autowired(required = false)
    private RemoteConfigLoader remoteConfigLoader;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (remoteConfigLoader != null) {
            remoteConfigLoader.close();
        }
    }
}