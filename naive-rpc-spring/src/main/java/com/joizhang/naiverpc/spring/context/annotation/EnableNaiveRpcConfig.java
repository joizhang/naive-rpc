package com.joizhang.naiverpc.spring.context.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(NaiveRpcConfigConfigurationRegistrar.class)
public @interface EnableNaiveRpcConfig {
}
