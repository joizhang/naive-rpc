package com.joizhang.naiverpc.spring.context.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@EnableNaiveRpcConfig
@NaiveRpcComponentScan
public @interface EnableNaiveRpc {

    @AliasFor(annotation = NaiveRpcComponentScan.class, attribute = "basePackages")
    String[] scanBasePackages() default {};

    @AliasFor(annotation = NaiveRpcComponentScan.class, attribute = "basePackageClasses")
    Class<?>[] scanBasePackageClasses() default {};

}
