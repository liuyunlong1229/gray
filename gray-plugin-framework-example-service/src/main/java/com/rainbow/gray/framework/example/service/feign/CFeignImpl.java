package com.rainbow.gray.framework.example.service.feign;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.rainbow.gray.framework.constant.GrayConstant;

@RestController
@ConditionalOnProperty(name = GrayConstant.SPRING_APPLICATION_NAME, havingValue = "discovery-springcloud-example-c")
public class CFeignImpl extends AbstractFeignImpl implements CFeign {
    private static final Logger LOG = LoggerFactory.getLogger(CFeignImpl.class);

    @Override
    public String invoke(@RequestBody String value) {
        value = doInvoke(value);

        LOG.info("调用路径：{}", value);

        return value;
    }
}