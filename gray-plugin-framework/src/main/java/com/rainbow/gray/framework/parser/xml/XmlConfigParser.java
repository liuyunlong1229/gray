package com.rainbow.gray.framework.parser.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rainbow.gray.framework.config.PluginConfigParser;
import com.rainbow.gray.framework.constant.ConfigConstant;
import com.rainbow.gray.framework.constant.GrayConstant;
import com.rainbow.gray.framework.entity.BlacklistEntity;
import com.rainbow.gray.framework.entity.DiscoveryEntity;
import com.rainbow.gray.framework.entity.RegionEntity;
import com.rainbow.gray.framework.entity.RegionFilterEntity;
import com.rainbow.gray.framework.entity.RegionWeightEntity;
import com.rainbow.gray.framework.entity.RuleEntity;
import com.rainbow.gray.framework.entity.VersionEntity;
import com.rainbow.gray.framework.entity.VersionFilterEntity;
import com.rainbow.gray.framework.entity.VersionWeightEntity;
import com.rainbow.gray.framework.entity.WeightEntity;
import com.rainbow.gray.framework.entity.WeightEntityWrapper;
import com.rainbow.gray.framework.entity.WeightFilterEntity;
import com.rainbow.gray.framework.entity.WeightType;
import com.rainbow.gray.framework.exception.GrayException;
import com.rainbow.gray.framework.parser.xml.dom4j.Dom4JReader;
import com.rainbow.gray.framework.utils.StringUtil;

public class XmlConfigParser implements PluginConfigParser {
    private static final Logger LOG = LoggerFactory.getLogger(XmlConfigParser.class);

    @Override
    public RuleEntity parse(String config) {
        if (StringUtils.isEmpty(config)) {
            throw new GrayException("Config is null or empty");
        }

        try {
            Document document = Dom4JReader.getDocument(config);

            Element rootElement = document.getRootElement();

            RuleEntity ruleEntity = parseRoot(config, rootElement);

            return ruleEntity;
        } catch (Exception e) {
            throw new GrayException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("rawtypes")
    private RuleEntity parseRoot(String config, Element element) {
        LOG.info("Start to parse rule xml...");

        int discoveryElementCount = element.elements(ConfigConstant.DISCOVERY_ELEMENT_NAME).size();
        if (discoveryElementCount > 1) {
            throw new GrayException("Allow only one element[" + ConfigConstant.DISCOVERY_ELEMENT_NAME + "] to be configed");
        }

        DiscoveryEntity discoveryEntity = null;
        for (Iterator elementIterator = element.elementIterator(); elementIterator.hasNext();) {
            Object childElementObject = elementIterator.next();
            if (childElementObject instanceof Element) {
                Element childElement = (Element) childElementObject;

                if (StringUtils.equals(childElement.getName(), ConfigConstant.DISCOVERY_ELEMENT_NAME)) {
                    discoveryEntity = new DiscoveryEntity();
                    parseDiscovery(childElement, discoveryEntity);
                }
            }
        }

        RuleEntity ruleEntity = new RuleEntity();
        ruleEntity.setDiscoveryEntity(discoveryEntity);
        ruleEntity.setContent(config);

        LOG.info("Rule content=\n{}", config);

        return ruleEntity;
    }

    @SuppressWarnings("rawtypes")
    private void parseDiscovery(Element element, DiscoveryEntity discoveryEntity) {
        for (Iterator elementIterator = element.elementIterator(); elementIterator.hasNext();) {
            Object childElementObject = elementIterator.next();
            if (childElementObject instanceof Element) {
                Element childElement = (Element) childElementObject;

                if (StringUtils.equals(childElement.getName(), ConfigConstant.BLACKLIST_ELEMENT_NAME)) {
                	parseBlacklist(childElement, discoveryEntity);
                } else if (StringUtils.equals(childElement.getName(), ConfigConstant.VERSION_ELEMENT_NAME)) {
                    parseVersionFilter(childElement, discoveryEntity);
                } else if (StringUtils.equals(childElement.getName(), ConfigConstant.REGION_ELEMENT_NAME)) {
                    parseRegionFilter(childElement, discoveryEntity);
                } else if (StringUtils.equals(childElement.getName(), ConfigConstant.WEIGHT_ELEMENT_NAME)) {
                    parseWeightFilter(childElement, discoveryEntity);
                }
            }
        }
    }


    @SuppressWarnings("rawtypes")
    private void parseBlacklist(Element element, DiscoveryEntity discoveryEntity) {
    	BlacklistEntity blacklistEntity = discoveryEntity.getBlacklistEntity();
        if (blacklistEntity != null) {
            throw new GrayException("Allow only one element[" + ConfigConstant.BLACKLIST_ELEMENT_NAME + "] to be configed");
        }
        blacklistEntity = new BlacklistEntity();
        
        List<String> idList = new ArrayList<String>();
        List<String> addressList = new ArrayList<String>();
        for (Iterator elementIterator = element.elementIterator(); elementIterator.hasNext();) {
            Object childElementObject = elementIterator.next();
            if (childElementObject instanceof Element) {
                Element childElement = (Element) childElementObject;

                Attribute valueAttribute = childElement.attribute(ConfigConstant.VALUE_ATTRIBUTE_NAME);
                if (valueAttribute == null) {
                    throw new GrayException("Attribute[" + ConfigConstant.VALUE_ATTRIBUTE_NAME + "] in element[" + childElement.getName() + "] is missing");
                }
                String value = valueAttribute.getData().toString().trim();
                List<String> valueList = StringUtil.splitToList(value, GrayConstant.SEPARATE);
                if (StringUtils.equals(childElement.getName(), ConfigConstant.ID_ELEMENT_NAME)) {
                    idList.addAll(valueList);
                } else if (StringUtils.equals(childElement.getName(), ConfigConstant.ADDRESS_ELEMENT_NAME)) {
                    addressList.addAll(valueList);
                }
            }
        }
        blacklistEntity.setIdList(idList);
        blacklistEntity.setAddressList(addressList);
        discoveryEntity.setBlacklistEntity(blacklistEntity);
    }


    @SuppressWarnings("rawtypes")
    private void parseVersionFilter(Element element, DiscoveryEntity discoveryEntity) {
        VersionFilterEntity versionFilterEntity = discoveryEntity.getVersionFilterEntity();
        if (versionFilterEntity != null) {
            throw new GrayException("Allow only one element[" + ConfigConstant.VERSION_ELEMENT_NAME + "] to be configed");
        }

        versionFilterEntity = new VersionFilterEntity();

        Map<String, List<VersionEntity>> versionEntityMap = versionFilterEntity.getVersionEntityMap();
        for (Iterator elementIterator = element.elementIterator(); elementIterator.hasNext();) {
            Object childElementObject = elementIterator.next();
            if (childElementObject instanceof Element) {
                Element childElement = (Element) childElementObject;

                if (StringUtils.equals(childElement.getName(), ConfigConstant.SERVICE_ELEMENT_NAME)) {
                    VersionEntity versionEntity = new VersionEntity();

                    Attribute consumerServiceNameAttribute = childElement.attribute(ConfigConstant.CONSUMER_SERVICE_NAME_ATTRIBUTE_NAME);
                    if (consumerServiceNameAttribute == null) {
                        throw new GrayException("Attribute[" + ConfigConstant.CONSUMER_SERVICE_NAME_ATTRIBUTE_NAME + "] in element[" + childElement.getName() + "] is missing");
                    }
                    String consumerServiceName = consumerServiceNameAttribute.getData().toString().trim();
                    versionEntity.setConsumerServiceName(consumerServiceName);

                    Attribute providerServiceNameAttribute = childElement.attribute(ConfigConstant.PROVIDER_SERVICE_NAME_ATTRIBUTE_NAME);
                    if (providerServiceNameAttribute == null) {
                        throw new GrayException("Attribute[" + ConfigConstant.PROVIDER_SERVICE_NAME_ATTRIBUTE_NAME + "] in element[" + childElement.getName() + "] is missing");
                    }
                    String providerServiceName = providerServiceNameAttribute.getData().toString().trim();
                    versionEntity.setProviderServiceName(providerServiceName);

                    Attribute consumerVersionValueAttribute = childElement.attribute(ConfigConstant.CONSUMER_VERSION_VALUE_ATTRIBUTE_NAME);
                    if (consumerVersionValueAttribute != null) {
                        String consumerVersionValue = consumerVersionValueAttribute.getData().toString().trim();
                        List<String> consumerVersionValueList = StringUtil.splitToList(consumerVersionValue, GrayConstant.SEPARATE);
                        versionEntity.setConsumerVersionValueList(consumerVersionValueList);
                    }

                    Attribute providerVersionValueAttribute = childElement.attribute(ConfigConstant.PROVIDER_VERSION_VALUE_ATTRIBUTE_NAME);
                    if (providerVersionValueAttribute != null) {
                        String providerVersionValue = providerVersionValueAttribute.getData().toString().trim();
                        List<String> providerVersionValueList = StringUtil.splitToList(providerVersionValue, GrayConstant.SEPARATE);
                        versionEntity.setProviderVersionValueList(providerVersionValueList);
                    }

                    List<VersionEntity> versionEntityList = versionEntityMap.get(consumerServiceName);
                    if (versionEntityList == null) {
                        versionEntityList = new ArrayList<VersionEntity>();
                        versionEntityMap.put(consumerServiceName, versionEntityList);
                    }

                    versionEntityList.add(versionEntity);
                }
            }
        }

        discoveryEntity.setVersionFilterEntity(versionFilterEntity);
    }

    @SuppressWarnings("rawtypes")
    private void parseRegionFilter(Element element, DiscoveryEntity discoveryEntity) {
        RegionFilterEntity regionFilterEntity = discoveryEntity.getRegionFilterEntity();
        if (regionFilterEntity != null) {
            throw new GrayException("Allow only one element[" + ConfigConstant.REGION_ELEMENT_NAME + "] to be configed");
        }

        regionFilterEntity = new RegionFilterEntity();

        Map<String, List<RegionEntity>> regionEntityMap = regionFilterEntity.getRegionEntityMap();
        for (Iterator elementIterator = element.elementIterator(); elementIterator.hasNext();) {
            Object childElementObject = elementIterator.next();
            if (childElementObject instanceof Element) {
                Element childElement = (Element) childElementObject;

                if (StringUtils.equals(childElement.getName(), ConfigConstant.SERVICE_ELEMENT_NAME)) {
                    RegionEntity regionEntity = new RegionEntity();

                    Attribute consumerServiceNameAttribute = childElement.attribute(ConfigConstant.CONSUMER_SERVICE_NAME_ATTRIBUTE_NAME);
                    if (consumerServiceNameAttribute == null) {
                        throw new GrayException("Attribute[" + ConfigConstant.CONSUMER_SERVICE_NAME_ATTRIBUTE_NAME + "] in element[" + childElement.getName() + "] is missing");
                    }
                    String consumerServiceName = consumerServiceNameAttribute.getData().toString().trim();
                    regionEntity.setConsumerServiceName(consumerServiceName);

                    Attribute providerServiceNameAttribute = childElement.attribute(ConfigConstant.PROVIDER_SERVICE_NAME_ATTRIBUTE_NAME);
                    if (providerServiceNameAttribute == null) {
                        throw new GrayException("Attribute[" + ConfigConstant.PROVIDER_SERVICE_NAME_ATTRIBUTE_NAME + "] in element[" + childElement.getName() + "] is missing");
                    }
                    String providerServiceName = providerServiceNameAttribute.getData().toString().trim();
                    regionEntity.setProviderServiceName(providerServiceName);

                    Attribute consumerRegionValueAttribute = childElement.attribute(ConfigConstant.CONSUMER_REGION_VALUE_ATTRIBUTE_NAME);
                    if (consumerRegionValueAttribute != null) {
                        String consumerRegionValue = consumerRegionValueAttribute.getData().toString().trim();
                        List<String> consumerRegionValueList = StringUtil.splitToList(consumerRegionValue, GrayConstant.SEPARATE);
                        regionEntity.setConsumerRegionValueList(consumerRegionValueList);
                    }

                    Attribute providerRegionValueAttribute = childElement.attribute(ConfigConstant.PROVIDER_REGION_VALUE_ATTRIBUTE_NAME);
                    if (providerRegionValueAttribute != null) {
                        String providerRegionValue = providerRegionValueAttribute.getData().toString().trim();
                        List<String> providerRegionValueList = StringUtil.splitToList(providerRegionValue, GrayConstant.SEPARATE);
                        regionEntity.setProviderRegionValueList(providerRegionValueList);
                    }

                    List<RegionEntity> regionEntityList = regionEntityMap.get(consumerServiceName);
                    if (regionEntityList == null) {
                        regionEntityList = new ArrayList<RegionEntity>();
                        regionEntityMap.put(consumerServiceName, regionEntityList);
                    }

                    regionEntityList.add(regionEntity);
                }
            }
        }

        discoveryEntity.setRegionFilterEntity(regionFilterEntity);
    }

    @SuppressWarnings("rawtypes")
    private void parseWeightFilter(Element element, DiscoveryEntity discoveryEntity) {
        WeightFilterEntity weightFilterEntity = discoveryEntity.getWeightFilterEntity();
        if (weightFilterEntity != null) {
            throw new GrayException("Allow only one element[" + ConfigConstant.WEIGHT_ELEMENT_NAME + "] to be configed");
        }

        weightFilterEntity = new WeightFilterEntity();

        Map<String, List<WeightEntity>> versionWeightEntityMap = new LinkedHashMap<String, List<WeightEntity>>();
        List<WeightEntity> versionWeightEntityList = new ArrayList<WeightEntity>();
        weightFilterEntity.setVersionWeightEntityMap(versionWeightEntityMap);
        weightFilterEntity.setVersionWeightEntityList(versionWeightEntityList);

        Map<String, List<WeightEntity>> regionWeightEntityMap = new LinkedHashMap<String, List<WeightEntity>>();
        List<WeightEntity> regionWeightEntityList = new ArrayList<WeightEntity>();
        weightFilterEntity.setRegionWeightEntityMap(regionWeightEntityMap);
        weightFilterEntity.setRegionWeightEntityList(regionWeightEntityList);

        for (Iterator elementIterator = element.elementIterator(); elementIterator.hasNext();) {
            Object childElementObject = elementIterator.next();
            if (childElementObject instanceof Element) {
                Element childElement = (Element) childElementObject;

                if (StringUtils.equals(childElement.getName(), ConfigConstant.SERVICE_ELEMENT_NAME)) {
                    WeightEntity weightEntity = new WeightEntity();

                    Attribute typeAttribute = childElement.attribute(ConfigConstant.TYPE_ATTRIBUTE_NAME);
                    if (typeAttribute == null) {
                        throw new GrayException("Attribute[" + ConfigConstant.TYPE_ATTRIBUTE_NAME + "] in element[" + childElement.getName() + "] is missing");
                    }
                    String type = typeAttribute.getData().toString().trim();
                    WeightType weightType = WeightType.fromString(type);
                    weightEntity.setType(weightType);

                    Attribute consumerServiceNameAttribute = childElement.attribute(ConfigConstant.CONSUMER_SERVICE_NAME_ATTRIBUTE_NAME);
                    String consumerServiceName = null;
                    if (consumerServiceNameAttribute != null) {
                        consumerServiceName = consumerServiceNameAttribute.getData().toString().trim();
                    }
                    weightEntity.setConsumerServiceName(consumerServiceName);

                    Attribute providerServiceNameAttribute = childElement.attribute(ConfigConstant.PROVIDER_SERVICE_NAME_ATTRIBUTE_NAME);
                    if (providerServiceNameAttribute == null) {
                        throw new GrayException("Attribute[" + ConfigConstant.PROVIDER_SERVICE_NAME_ATTRIBUTE_NAME + "] in element[" + childElement.getName() + "] is missing");
                    }
                    String providerServiceName = providerServiceNameAttribute.getData().toString().trim();
                    weightEntity.setProviderServiceName(providerServiceName);

                    Attribute providerWeightValueAttribute = childElement.attribute(ConfigConstant.PROVIDER_WEIGHT_VALUE_ATTRIBUTE_NAME);
                    if (providerWeightValueAttribute == null) {
                        throw new GrayException("Attribute[" + ConfigConstant.PROVIDER_WEIGHT_VALUE_ATTRIBUTE_NAME + "] in element[" + childElement.getName() + "] is missing");
                    }
                    String providerWeightValue = providerWeightValueAttribute.getData().toString().trim();

                    WeightEntityWrapper.parseWeightEntity(weightEntity, providerWeightValue);

                    if (StringUtils.isNotEmpty(consumerServiceName)) {
                        if (weightType == WeightType.VERSION) {
                            List<WeightEntity> list = versionWeightEntityMap.get(consumerServiceName);
                            if (list == null) {
                                list = new ArrayList<WeightEntity>();
                                versionWeightEntityMap.put(consumerServiceName, list);
                            }

                            list.add(weightEntity);
                        } else if (weightType == WeightType.REGION) {
                            List<WeightEntity> list = regionWeightEntityMap.get(consumerServiceName);
                            if (list == null) {
                                list = new ArrayList<WeightEntity>();
                                regionWeightEntityMap.put(consumerServiceName, list);
                            }

                            list.add(weightEntity);
                        }
                    } else {
                        if (weightType == WeightType.VERSION) {
                            versionWeightEntityList.add(weightEntity);
                        } else if (weightType == WeightType.REGION) {
                            regionWeightEntityList.add(weightEntity);
                        }
                    }
                } else if (StringUtils.equals(childElement.getName(), ConfigConstant.VERSION_ELEMENT_NAME)) {
                    VersionWeightEntity versionWeightEntity = weightFilterEntity.getVersionWeightEntity();
                    if (versionWeightEntity != null) {
                        throw new GrayException("Allow only one element[" + ConfigConstant.VERSION_ELEMENT_NAME + "] to be configed");
                    }

                    versionWeightEntity = new VersionWeightEntity();

                    Attribute providerWeightValueAttribute = childElement.attribute(ConfigConstant.PROVIDER_WEIGHT_VALUE_ATTRIBUTE_NAME);
                    if (providerWeightValueAttribute == null) {
                        throw new GrayException("Attribute[" + ConfigConstant.PROVIDER_WEIGHT_VALUE_ATTRIBUTE_NAME + "] in element[" + childElement.getName() + "] is missing");
                    }
                    String providerWeightValue = providerWeightValueAttribute.getData().toString().trim();

                    WeightEntityWrapper.parseWeightEntity(versionWeightEntity, providerWeightValue);

                    weightFilterEntity.setVersionWeightEntity(versionWeightEntity);
                } else if (StringUtils.equals(childElement.getName(), ConfigConstant.REGION_ELEMENT_NAME)) {
                    RegionWeightEntity regionWeightEntity = weightFilterEntity.getRegionWeightEntity();
                    if (regionWeightEntity != null) {
                        throw new GrayException("Allow only one element[" + ConfigConstant.REGION_ELEMENT_NAME + "] to be configed");
                    }

                    regionWeightEntity = new RegionWeightEntity();

                    Attribute providerWeightValueAttribute = childElement.attribute(ConfigConstant.PROVIDER_WEIGHT_VALUE_ATTRIBUTE_NAME);
                    if (providerWeightValueAttribute == null) {
                        throw new GrayException("Attribute[" + ConfigConstant.PROVIDER_WEIGHT_VALUE_ATTRIBUTE_NAME + "] in element[" + childElement.getName() + "] is missing");
                    }
                    String providerWeightValue = providerWeightValueAttribute.getData().toString().trim();

                    WeightEntityWrapper.parseWeightEntity(regionWeightEntity, providerWeightValue);

                    weightFilterEntity.setRegionWeightEntity(regionWeightEntity);
                }
            }
        }

        discoveryEntity.setWeightFilterEntity(weightFilterEntity);
    }
}
