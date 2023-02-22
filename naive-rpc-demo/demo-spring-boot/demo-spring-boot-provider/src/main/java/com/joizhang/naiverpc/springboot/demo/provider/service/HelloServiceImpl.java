package com.joizhang.naiverpc.springboot.demo.provider.service;

import com.joizhang.naiverpc.spring.annotation.NaiveRpcService;
import com.joizhang.naiverpc.springboot.demo.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NaiveRpcService
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        String msg = "Hello, " + name;
        log.info(msg);
        return msg;
    }

}
