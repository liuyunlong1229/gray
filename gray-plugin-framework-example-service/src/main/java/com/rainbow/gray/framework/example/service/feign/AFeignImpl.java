package com.rainbow.gray.framework.example.service.feign;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.rainbow.gray.framework.constant.GrayConstant;

@RestController
@ConditionalOnProperty(name = GrayConstant.SPRING_APPLICATION_NAME, havingValue = "discovery-springcloud-example-a")
public class AFeignImpl extends AbstractFeignImpl implements AFeign {
    private static final Logger LOG = LoggerFactory.getLogger(AFeignImpl.class);

    @Autowired
    private BFeign bFeign;

    // Hystrix测试
    // @Autowired
    // private HystrixService hystrixService;

    @Override
    public String invoke(@RequestBody String value) {
        // LOG.info("---------- 主方法里获取上下文 RequestContextHolder.getRequestAttributes()：{}", RequestContextHolder.getRequestAttributes());
        // LOG.info("---------- 主方法里获取上下文 RestStrategyContext.getCurrentContext().getRequestAttributes()：{}", RestStrategyContext.getCurrentContext().getRequestAttributes());
        // LOG.info("---------- 主方法里获取Token：{}", ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("token"));

        // hystrixService.invokeHystrix(value);

        value = doInvoke(value);
        value = bFeign.invoke(value);

        LOG.info("调用路径：{}", value);

        return value;
    }
}