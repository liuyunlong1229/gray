package com.rainbow.gray.framework.config;

import com.rainbow.gray.framework.entity.RuleEntity;

public interface PluginConfigParser {
    RuleEntity parse(String config);
}