package com.rainbow.gray.framework.ruleweight;



import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.rainbow.gray.framework.exception.GrayException;

public class MapWeightRandom<K, V extends Number> {
    private TreeMap<Double, K> weightMap = new TreeMap<Double, K>();

    public MapWeightRandom(List<Pair<K, V>> pairlist) {
        for (Pair<K, V> pair : pairlist) {
            double value = pair.getValue().doubleValue();
            if (value <= 0) {
                continue;
            }

            double lastWeight = weightMap.size() == 0 ? 0 : weightMap.lastKey().doubleValue();
            weightMap.put(value + lastWeight, pair.getKey());
        }
    }

    public K random() {
    	
        if (MapUtils.isEmpty(weightMap)) {
        	throw new GrayException("Weight values are all <= 0 or invalid format");
        }

        double randomWeight = weightMap.lastKey() * Math.random();
        SortedMap<Double, K> tailMap = weightMap.tailMap(randomWeight, false);

        return weightMap.get(tailMap.firstKey());
    }
}