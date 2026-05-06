package com.joizhang.naiverpc.spring.context.annotation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.joizhang.naiverpc.spring.context.annotation.api.HelloService;
import com.joizhang.naiverpc.spring.context.annotation.provider.ProviderConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class NaiveRpcComponentScanRegistrarTest {

    @Test
    public void testNaiveRpcComponentScanRegistrar() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
            context.register(ProviderConfiguration.class);
            context.refresh();
            HelloService helloService = context.getBean(HelloService.class);
            String value = helloService.sayHello("Joi");
            assertEquals("Hello, Joi", value);
        }
    }
}
