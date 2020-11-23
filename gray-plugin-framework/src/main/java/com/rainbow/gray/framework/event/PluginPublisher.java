package com.rainbow.gray.framework.event;

import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.eventbus.core.EventControllerFactory;

public class PluginPublisher {
    @Autowired
    private EventControllerFactory eventControllerFactory;

    public void asyncPublish(Object object) {
        eventControllerFactory.getAsyncController().post(object);
    }

    public void syncPublish(Object object) {
        eventControllerFactory.getSyncController().post(object);
    }
}