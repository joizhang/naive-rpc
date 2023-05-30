package com.joizhang.naiverpc.spring.context.annotation;

import com.joizhang.naiverpc.spring.context.NaiveRpcSpringInitializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class NaiveRpcConfigConfigurationRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata,
                                        @NotNull BeanDefinitionRegistry registry) {
        NaiveRpcSpringInitializer.initialize(registry);
    }

}
