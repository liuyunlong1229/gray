package com.rainbow.gray.framework.example.service.feign;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.rainbow.gray.framework.constant.GrayConstant;

@RestController
@ConditionalOnProperty(name = GrayConstant.SPRING_APPLICATION_NAME, havingValue = "discovery-springcloud-example-b")
public class BFeignImpl extends AbstractFeignImpl implements BFeign {
    private static final Logger LOG = LoggerFactory.getLogger(BFeignImpl.class);

    @Autowired
    private CFeign cFeign;

    @Override
    public String invoke(@RequestBody String value) {
        value = doInvoke(value);
        value = cFeign.invoke(value);

        LOG.info("调用路径：{}", value);

        return value;
    }
}