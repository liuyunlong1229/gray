package com.rainbow.gray.framework.loader;



public abstract class RemoteConfigLoader implements ConfigLoader {
    public abstract String getConfigType();

    public abstract void close();
}