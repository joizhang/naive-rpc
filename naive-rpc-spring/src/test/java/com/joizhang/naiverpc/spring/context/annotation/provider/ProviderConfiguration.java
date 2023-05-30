package com.joizhang.naiverpc.spring.context.annotation.provider;

import com.joizhang.naiverpc.spring.context.annotation.EnableNaiveRpcConfig;
import com.joizhang.naiverpc.spring.context.annotation.NaiveRpcComponentScan;
import org.springframework.context.annotation.PropertySource;

@EnableNaiveRpcConfig
@NaiveRpcComponentScan(basePackages = "com.joizhang.naiverpc.spring.context.annotation.provider")
//@NaiveRpcComponentScan
@PropertySource("classpath:/META-INF/default.properties")
public class ProviderConfiguration {
}
