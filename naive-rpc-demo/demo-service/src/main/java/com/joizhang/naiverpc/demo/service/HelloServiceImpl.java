package com.joizhang.naiverpc.demo.service;

import com.joizhang.naiverpc.demo.api.service.HelloService;

public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello, " + name;
    }

}
