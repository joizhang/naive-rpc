package com.joizhang.naiverpc.spring.beans.factory.annotation;

import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

public class ServiceBeanNameBuilder {

    private static final String SEPARATOR = ":";

    // Required
    private final String interfaceClassName;

    private final Environment environment;

    private ServiceBeanNameBuilder(Class<?> interfaceClass, Environment environment) {
        this(interfaceClass.getName(), environment);
    }

    private ServiceBeanNameBuilder(String interfaceClassName, Environment environment) {
        this.interfaceClassName = interfaceClassName;
        this.environment = environment;
    }

    private ServiceBeanNameBuilder(AnnotationAttributes attributes, Class<?> defaultInterfaceClass, Environment environment) {
        this(ServiceAnnotationUtils.resolveInterfaceName(attributes, defaultInterfaceClass), environment);
    }

    public static ServiceBeanNameBuilder create(AnnotationAttributes attributes, Class<?> defaultInterfaceClass, Environment environment) {
        return new ServiceBeanNameBuilder(attributes, defaultInterfaceClass, environment);
    }

    public static ServiceBeanNameBuilder create(Class<?> interfaceClass, Environment environment) {
        return new ServiceBeanNameBuilder(interfaceClass, environment);
    }

    public static ServiceBeanNameBuilder create(String interfaceClass, Environment environment) {
        return new ServiceBeanNameBuilder(interfaceClass, environment);
    }

    private static void append(StringBuilder builder, String value) {
        builder.append(SEPARATOR);
        if (StringUtils.hasText(value)) {
            builder.append(value);
        }
    }

    public String build() {
        StringBuilder beanNameBuilder = new StringBuilder("ServiceBean");
        // Required
        append(beanNameBuilder, interfaceClassName);
        // Build and remove last ":"
        String rawBeanName = beanNameBuilder.toString();
        // Resolve placeholders
        return environment.resolvePlaceholders(rawBeanName);
    }

}
