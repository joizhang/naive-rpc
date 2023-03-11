package com.joizhang.naiverpc.spring.context.annotation.provider;

import com.joizhang.naiverpc.spring.context.annotation.NaiveRpcComponentScan;
import org.springframework.context.annotation.PropertySource;

@NaiveRpcComponentScan(basePackages = "com.joizhang.naiverpc.spring.context.annotation.provider")
@PropertySource("classpath:/META-INF/default.properties")
public class ProviderConfiguration {
}
