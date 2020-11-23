package com.rainbow.gray.framework.matcher;

public interface DiscoveryMatcherStrategy {
    boolean match(String pattern, String value);
}