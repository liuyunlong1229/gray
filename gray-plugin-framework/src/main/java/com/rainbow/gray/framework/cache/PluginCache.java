package com.rainbow.gray.framework.cache;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

public class PluginCache {
    private LoadingCache<String, String> loadingCache;

    public PluginCache() {
        loadingCache = Caffeine.newBuilder()
                .expireAfterWrite(365 * 100, TimeUnit.DAYS)
                .initialCapacity(10)
                .maximumSize(100)
                .recordStats()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        return StringUtils.EMPTY;
                    }
                });
    }

    public boolean put(String key, String value) {
        loadingCache.put(key, value);

        return Boolean.TRUE;
    }

    public String get(String key) {
        try {
            return loadingCache.get(key);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    public boolean clear(String key) {
        loadingCache.invalidate(key);

        return Boolean.TRUE;
    }
}