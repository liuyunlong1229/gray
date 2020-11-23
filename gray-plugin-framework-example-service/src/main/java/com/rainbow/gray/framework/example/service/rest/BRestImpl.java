package com.rainbow.gray.framework.example.service.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.rainbow.gray.framework.constant.GrayConstant;

@RestController
@ConditionalOnProperty(name = GrayConstant.SPRING_APPLICATION_NAME, havingValue = "discovery-springcloud-example-b")
public class BRestImpl extends AbstractRestImpl {
    private static final Logger LOG = LoggerFactory.getLogger(BRestImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(path = "/rest", method = RequestMethod.POST)
//    @SentinelResource(value = "sentinel-resource", blockHandler = "handleBlock", fallback = "handleFallback")
    public String rest(@RequestBody String value) {
        value = doRest(value);

        LOG.info("New token=Token-B");

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", "Token-B");

        HttpEntity<String> entity = new HttpEntity<String>(value, headers);
        value = restTemplate.postForEntity("http://discovery-springcloud-example-c/rest", entity, String.class).getBody();

        LOG.info("调用路径：{}", value);

        return value;
    }

    @RequestMapping(path = "/test", method = RequestMethod.POST)
    public String test(@RequestBody String value) {
        return value;
    }

//    public String handleBlock(String value, BlockException e) {
//        return "B server sentinel block, cause=" + e.getClass().getName() + ", rule=" + e.getRule() + ", limitApp=" + e.getRuleLimitApp() + ", value=" + value;
//    }
//
//    public String handleFallback(String value) {
//        return "B server sentinel fallback, value=" + value;
//    }
}