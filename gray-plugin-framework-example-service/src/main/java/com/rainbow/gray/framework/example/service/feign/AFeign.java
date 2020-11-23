package com.rainbow.gray.framework.example.service.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "discovery-springcloud-example-a")
// Context-patch一旦被设置，在Feign也要带上context-path，外部Postman调用网关或者服务路径也要带context-path
// @FeignClient(value = "discovery-springcloud-example-a", path = "/nepxion")
// @FeignClient(value = "discovery-springcloud-example-a/nepxion")
public interface AFeign {
    @RequestMapping(path = "/invoke", method = RequestMethod.POST)
    String invoke(@RequestBody String value);
}