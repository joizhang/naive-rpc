package com.joizhang.naiverpc.spring.beans.factory.annotation;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class AbstractAnnotationBeanPostProcessor implements MergedBeanDefinitionPostProcessor,
        BeanFactoryAware, BeanClassLoaderAware, EnvironmentAware, DisposableBean {

    @Override
    public void setBeanClassLoader(@NotNull ClassLoader classLoader) {

    }

    @Override
    public void setBeanFactory(@NotNull BeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void postProcessMergedBeanDefinition(@NotNull RootBeanDefinition beanDefinition,
                                                @NotNull Class<?> beanType,
                                                @NotNull String beanName) {

    }

    @Override
    public void setEnvironment(@NotNull Environment environment) {

    }
}
