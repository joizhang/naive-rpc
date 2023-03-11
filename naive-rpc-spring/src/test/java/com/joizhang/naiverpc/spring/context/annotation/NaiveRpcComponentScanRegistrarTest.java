package com.joizhang.naiverpc.spring.context.annotation;

import com.joizhang.naiverpc.spring.context.annotation.api.HelloService;
import com.joizhang.naiverpc.spring.context.annotation.provider.ProviderConfiguration;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.Assert.*;

public class NaiveRpcComponentScanRegistrarTest {

    @Test
    public void testNaiveRpcComponentScanRegistrar() {
        AnnotationConfigApplicationContext providerContext = new AnnotationConfigApplicationContext();
        providerContext.register(ProviderConfiguration.class);
        providerContext.refresh();
        HelloService helloService = providerContext.getBean(HelloService.class);
        String value = helloService.sayHello("Joi");
        assertEquals("Hello, Joi", value);
    }

}