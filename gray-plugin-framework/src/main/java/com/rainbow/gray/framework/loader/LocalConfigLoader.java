package com.rainbow.gray.framework.loader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.rainbow.gray.framework.utils.FileContextUtil;

public abstract class LocalConfigLoader implements ConfigLoader {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public String[] getConfigList() throws Exception {
        String path = getPath();

        String[] config = new String[1];
        config[0] = FileContextUtil.getText(applicationContext, path);

        return config;
    }

    protected abstract String getPath();
}