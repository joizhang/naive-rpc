package com.joizhang.naiverpc.demo.service;

import com.joizhang.naiverpc.demo.api.service.HelloService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        String msg = "Hello, " + name;
        log.info(msg);
        return msg;
    }

}
