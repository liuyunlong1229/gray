package com.rainbow.gray.framework.filter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pateo.qingcloud6.framework.support.RequestHeaderHolder;
import com.pateo.qingcloud6.framework.thread.RequestHeaderAttributes;
import com.rainbow.gray.framework.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yunlong.liu
 * @date 2020-11-23 15:36:38
 */

public class HeaderServerFilter extends AbstractServerFilter {

   private static  final Logger  LOGGER= LoggerFactory.getLogger(HeaderServerFilter.class);
    @Override
    public void onGetInstances(String serviceId, List<ServiceInstance> instances) {
        applyHeaderFilter(serviceId, instances);
    }

    @Override
    public void onGetServices(List<String> services) {

    }

    @Override
    public int getOrder() {
         // After Zone filter
        return HIGHEST_PRECEDENCE + 4;
    }



    private void applyHeaderFilter(String providerServiceId, List<ServiceInstance> instances) {

        // 如果请求头中指定的服务列表为空
        String serviceList=getServiceListFromHeader();

        if (StringUtils.isEmpty(serviceList)) {
            return;
        }
       List<ServicelistEntity>  servicelistEntities= new Gson().fromJson(serviceList, new TypeToken<List<ServicelistEntity>>() {
        }.getType());

        //如果配置的服务列表为空
        if (servicelistEntities == null || servicelistEntities.isEmpty()) {
            return;
        }
        List<ServicelistEntity> requiredProvideServiceList= servicelistEntities.stream().filter(e->e.getServiceId().equals(providerServiceId)).collect(Collectors.toList());

        if (requiredProvideServiceList == null || requiredProvideServiceList.isEmpty()) {
            return;
        }

        ServicelistEntity servicelistEntity=requiredProvideServiceList.get(0);
        ServicelistEntity.PodLabel podLabel= servicelistEntity.getPodLabel();

        // 当前版本的消费端所能调用提供端的版本号列表
        List<String> allNoFilterValueList = null;

        Iterator<ServiceInstance> iterator = instances.iterator();

        List<ServiceInstance> matchedServer=new ArrayList<>();
        while (iterator.hasNext()) {
            ServiceInstance instance = iterator.next();
            Map<String,String> metadata= pluginAdapter.getInstanceMetadata(instance);
            String appChannel= metadata.get("appChannel");
            String appVer=metadata.get("appVer");
            if(podLabel.getAppChannel().equalsIgnoreCase(appChannel) && podLabel.getAppVer().equalsIgnoreCase(appVer)){
                matchedServer.add(instance);
                LOGGER.info("负载均衡过滤器，根据请求头过滤服务[{}]成功，过滤的appChannel=={},appVer=={}",providerServiceId,appChannel,appVer);
            }
        }

        //如果找到对应的版本，则保匹配的版本，否则不做处理
        if (!matchedServer.isEmpty()) {
            instances=matchedServer;
        }else{
            LOGGER.warn("负载均衡过滤器，根据请求头无法找到appChannel={},appVer={}过滤的服务,将不做处理",podLabel.getAppChannel(),podLabel.getAppVer(),providerServiceId);
        }
    }

    private String getServiceListFromHeader() {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if ( attributes != null) {
             HttpServletRequest request = attributes.getRequest();
            return  request.getHeader("Service-List");
        }else {
            RequestHeaderAttributes requestHeaderAttributes = RequestHeaderHolder.getRequestAttributes();
            if (requestHeaderAttributes != null) {
                return requestHeaderAttributes.getHeader("Service-List");
            }
        }
        return null;
    }
}
