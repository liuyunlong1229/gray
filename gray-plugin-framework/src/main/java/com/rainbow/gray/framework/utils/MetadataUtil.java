package com.rainbow.gray.framework.utils;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.rainbow.gray.framework.constant.GrayConstant;

public class MetadataUtil {
    public static void filter(Map<String, String> metadata) {
        Properties properties = System.getProperties();
        Set<String> propertyNames = properties.stringPropertyNames();
        for (String propertyName : propertyNames) {
            if (propertyName.startsWith(GrayConstant.METADATA + ".")) {
                String key = propertyName.substring((GrayConstant.METADATA + ".").length());
                String value = properties.get(propertyName).toString();
                metadata.put(key, value);
            }
        }
    }

    public static void filter(List<String> metadata) {
        Properties properties = System.getProperties();
        Set<String> propertyNames = properties.stringPropertyNames();
        for (String propertyName : propertyNames) {
            if (propertyName.startsWith(GrayConstant.METADATA + ".")) {
                String key = propertyName.substring((GrayConstant.METADATA + ".").length());
                String value = properties.get(propertyName).toString();

                int index = getIndex(metadata, key);
                if (index > -1) {
                    metadata.set(index, key + "=" + value);
                } else {
                    metadata.add(key + "=" + value);
                }
            }
        }
    }

    public static int getIndex(List<String> metadata, String key) {
        for (int i = 0; i < metadata.size(); i++) {
            String value = metadata.get(i);
            if (value.startsWith(key + "=")) {
                return i;
            }
        }

        return -1;
    }

    public static boolean containsKey(List<String> metadata, String key) {
        for (String value : metadata) {
            if (value.startsWith(key + "=")) {
                return true;
            }
        }

        return false;
    }
}