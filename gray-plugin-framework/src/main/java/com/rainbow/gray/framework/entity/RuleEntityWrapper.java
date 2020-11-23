package com.rainbow.gray.framework.entity;



public class RuleEntityWrapper {
    // 以局部规则为准，如果局部规则规则里某些属性没有，而全局规则有，则从全局规则把那些属性复制到局部规则，并合并创建最终的复合规则
    public static RuleEntity assemble(RuleEntity partialRuleEntity, RuleEntity globalRuleEntity) {
        RuleEntity ruleEntity = new RuleEntity();

        DiscoveryEntity discoveryEntity = null;
        if (partialRuleEntity != null && partialRuleEntity.getDiscoveryEntity() != null) {
            discoveryEntity = partialRuleEntity.getDiscoveryEntity();
        } else if (globalRuleEntity != null && globalRuleEntity.getDiscoveryEntity() != null) {
            discoveryEntity = globalRuleEntity.getDiscoveryEntity();
        }
        ruleEntity.setDiscoveryEntity(discoveryEntity);

        return ruleEntity;
    }
}