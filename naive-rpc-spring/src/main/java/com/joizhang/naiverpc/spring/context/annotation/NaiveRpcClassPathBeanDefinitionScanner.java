package com.joizhang.naiverpc.spring.context.annotation;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NaiveRpcClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    /**
     * key is package to scan, value is BeanDefinition
     */
    private final ConcurrentMap<String, Set<BeanDefinition>> beanDefinitionMap = new ConcurrentHashMap<>();

    public NaiveRpcClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters,
                                                  Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
        AnnotationConfigUtils.registerAnnotationConfigProcessors(registry);
    }

    public NaiveRpcClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry, Environment environment,
                                                  ResourceLoader resourceLoader) {
        this(registry, false, environment, resourceLoader);
    }

    @NotNull
    @Override
    public Set<BeanDefinition> findCandidateComponents(@NotNull String basePackage) {
        Set<BeanDefinition> beanDefinitions = beanDefinitionMap.get(basePackage);
        if (Objects.isNull(beanDefinitions)) {
            beanDefinitions = super.findCandidateComponents(basePackage);
            beanDefinitionMap.put(basePackage, beanDefinitions);
        }
        return beanDefinitions;
    }
}
