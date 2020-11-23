package com.rainbow.gray.framework.ruleweight;

import java.util.List;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;

public class WeightedBalancer extends Balancer {
	
	/**
     * 根据随机权重策略, 从一群服务器中选择一个
     * @return
     */
    public static Instance chooseInstanceByRandomWeight (List<Instance> instanceList) {
        // 这是父类Balancer自带的根据随机权重获取服务的方法.
        return getHostByRandomWeight(instanceList);
    }

}
