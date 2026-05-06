package com.joizhang.naiverpc.spring.context.annotation;

import java.lang.annotation.*;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(NaiveRpcConfigConfigurationRegistrar.class)
public @interface EnableNaiveRpcConfig {}
