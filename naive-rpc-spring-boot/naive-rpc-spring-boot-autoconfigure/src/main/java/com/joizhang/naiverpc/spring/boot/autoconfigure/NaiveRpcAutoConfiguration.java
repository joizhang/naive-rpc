package com.joizhang.naiverpc.spring.boot.autoconfigure;

import com.joizhang.naiverpc.spring.boot.util.NaiveRpcUtils;
import com.joizhang.naiverpc.spring.context.annotation.EnableNaiveRpcConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = NaiveRpcUtils.NAIVE_RPC_PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(NaiveRpcConfigurationProperties.class)
@EnableNaiveRpcConfig
public class NaiveRpcAutoConfiguration {}
