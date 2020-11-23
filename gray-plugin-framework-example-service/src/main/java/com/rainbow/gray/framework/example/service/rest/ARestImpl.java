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
@ConditionalOnProperty(name = GrayConstant.SPRING_APPLICATION_NAME, havingValue = "discovery-springcloud-example-a")
public class ARestImpl extends AbstractRestImpl {
    private static final Logger LOG = LoggerFactory.getLogger(ARestImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(path = "/rest", method = RequestMethod.POST)
//    @SentinelResource(value = "sentinel-resource", blockHandler = "handleBlock", fallback = "handleFallback")
    public String rest(@RequestBody String value) {
        value = doRest(value);

        // Just for testing
//        ServletRequestAttributes attributes = serviceStrategyContextHolder.getRestAttributes();
//        Enumeration<String> headerNames = attributes.getRequest().getHeaderNames();

//        System.out.println("Header name list:");
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            System.out.println("* " + headerName);
//        }

//        String token = attributes.getRequest().getHeader("token");
//        System.out.println("Old token=" + token);

        System.out.println("New token=Token-A");

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", "Token-A");

        HttpEntity<String> entity = new HttpEntity<String>(value, headers);
        value = restTemplate.postForEntity("http://discovery-springcloud-example-b/rest", entity, String.class).getBody();

        LOG.info("调用路径：{}", value);

        return value;
    }

    @RequestMapping(path = "/test", method = RequestMethod.POST)
    public String test(@RequestBody String value) {
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

//    public String handleBlock(String value, BlockException e) {
//        return "A server sentinel block, cause=" + e.getClass().getName() + ", rule=" + e.getRule() + ", limitApp=" + e.getRuleLimitApp() + ", value=" + value;
//    }
//
//    public String handleFallback(String value) {
//        return "A server sentinel fallback, value=" + value;
//    }
}