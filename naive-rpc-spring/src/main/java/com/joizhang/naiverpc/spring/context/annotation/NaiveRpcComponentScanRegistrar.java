package com.joizhang.naiverpc.spring.context.annotation;

import com.joizhang.naiverpc.spring.beans.factory.annotation.ServiceAnnotationPostProcessor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class NaiveRpcComponentScanRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(@NotNull AnnotationMetadata importingClassMetadata,
                                        @NotNull BeanDefinitionRegistry registry) {
        Set<String> packagesToScan = getPackagesToScan(importingClassMetadata);
        registerServiceAnnotationPostProcessor(packagesToScan, registry);
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        // get from @NaiveRpcComponentScan
        Set<String> packagesToScan = getPackagesToScan0(metadata, NaiveRpcComponentScan.class,
                "basePackages", "basePackageClasses"
        );
        // get from @EnableDubbo, compatible with spring 3.x
        if (packagesToScan.isEmpty()) {
            packagesToScan = getPackagesToScan0(metadata, EnableNaiveRpc.class,
                    "scanBasePackages", "scanBasePackageClasses");
        }
        if (packagesToScan.isEmpty()) {
            return Collections.singleton(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return packagesToScan;
    }

    private Set<String> getPackagesToScan0(AnnotationMetadata metadata, Class<?> annotationClass,
                                           String basePackagesName, String basePackageClassesName) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(annotationClass.getName()));
        if (attributes == null) {
            return Collections.emptySet();
        }
        // basePackages
        String[] basePackages = attributes.getStringArray(basePackagesName);
        Set<String> packagesToScan = new LinkedHashSet<>(Arrays.asList(basePackages));
        // basePackageClasses
        Class<?>[] basePackageClasses = attributes.getClassArray(basePackageClassesName);
        for (Class<?> basePackageClass : basePackageClasses) {
            packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
        }
        // value
        if (attributes.containsKey("value")) {
            String[] value = attributes.getStringArray("value");
            packagesToScan.addAll(Arrays.asList(value));
        }
        return packagesToScan;
    }

    private void registerServiceAnnotationPostProcessor(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ServiceAnnotationPostProcessor.class);
        builder.addConstructorArgValue(packagesToScan);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }
}
