package com.joizhang.naiverpc.spring.beans.factory.annotation;

import com.joizhang.naiverpc.spring.context.annotation.NaiveRpcComponentScan;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

@ContextConfiguration(classes = {
        ServiceAnnotationTestConfiguration.class,
        ServiceAnnotationPostProcessorTest.class,
        ServiceAnnotationPostProcessorTest.DuplicatedScanConfig.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "provider.package = org.apache.dubbo.config.spring.context.annotation.provider",
})
public class ServiceAnnotationPostProcessorTest {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @Test
    public void testServiceAnnotationPostProcessor() {
        Map<String, ServiceAnnotationPostProcessorTest> beanPostProcessorsMap =
                beanFactory.getBeansOfType(ServiceAnnotationPostProcessorTest.class);
        Assert.assertEquals(2, beanPostProcessorsMap.size());
    }

    @NaiveRpcComponentScan({"org.apache.dubbo.config.spring.context.annotation", "${provider.package}"})
    static class DuplicatedScanConfig {

    }
}