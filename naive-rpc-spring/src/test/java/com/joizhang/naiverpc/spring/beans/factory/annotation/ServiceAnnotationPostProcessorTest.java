package com.joizhang.naiverpc.spring.beans.factory.annotation;

import com.joizhang.naiverpc.spring.context.annotation.EnableNaiveRpc;
import com.joizhang.naiverpc.spring.context.annotation.NaiveRpcComponentScan;
import com.joizhang.naiverpc.spring.context.annotation.api.HelloService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        ServiceAnnotationTestConfiguration.class,
        ServiceAnnotationPostProcessorTest.class,
        ServiceAnnotationPostProcessorTest.DuplicatedScanConfig.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "provider.package = com.joizhang.naiverpc.spring.context.annotation.provider",
})
@EnableNaiveRpc(scanBasePackages = "${provider.package}")
public class ServiceAnnotationPostProcessorTest {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Test
    public void testServiceAnnotationPostProcessor() {
        Map<String, HelloService> helloServicesMap = beanFactory.getBeansOfType(HelloService.class);
        assertEquals(1, helloServicesMap.size());

        Map<String, ServiceAnnotationPostProcessorTest> beanPostProcessorsMap =
                beanFactory.getBeansOfType(ServiceAnnotationPostProcessorTest.class);
        assertEquals(1, beanPostProcessorsMap.size());
    }

    @NaiveRpcComponentScan({"org.apache.dubbo.config.spring.context.annotation", "${provider.package}"})
    static class DuplicatedScanConfig {

    }
}