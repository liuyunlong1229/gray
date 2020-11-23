package com.rainbow.gray.framework.cache;



import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.rainbow.gray.framework.entity.RuleEntity;

public class RuleCache {
    private LoadingCache<String, RuleEntity> loadingCache;

    public RuleCache() {
        loadingCache = Caffeine.newBuilder()
                .expireAfterWrite(365 * 100, TimeUnit.DAYS)
                .initialCapacity(2)
                .maximumSize(10)
                .recordStats()
                .build(new CacheLoader<String, RuleEntity>() {
                    @Override
                    public RuleEntity load(String key) throws Exception {
                        return null;
                    }
                });
    }

    public boolean put(String key, RuleEntity ruleEntity) {
        loadingCache.put(key, ruleEntity);

        return Boolean.TRUE;
    }

    public RuleEntity get(String key) {
        try {
            return loadingCache.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean clear(String key) {
        loadingCache.invalidate(key);

        return Boolean.TRUE;
    }
}