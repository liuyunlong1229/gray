package com.rainbow.gray.framework.matcher;

import org.springframework.util.AntPathMatcher;

public class DiscoveryAntPathMatcherStrategy implements DiscoveryMatcherStrategy {
    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public boolean match(String pattern, String value) {
        return matcher.match(pattern, value);
    }
}