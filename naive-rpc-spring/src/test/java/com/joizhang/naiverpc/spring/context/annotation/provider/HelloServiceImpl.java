package com.joizhang.naiverpc.spring.context.annotation.provider;

import com.joizhang.naiverpc.spring.annotation.NaiveRpcService;
import com.joizhang.naiverpc.spring.context.annotation.api.HelloService;

@NaiveRpcService(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
