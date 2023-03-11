package com.joizhang.naiverpc.spring.beans.factory.annotation;

import com.joizhang.naiverpc.config.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/META-INF/default.properties")
public class ServiceAnnotationTestConfiguration {

    @Bean("naiverpc-demo-application")
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("naiverpc-demo-application");
        return applicationConfig;
    }

}
