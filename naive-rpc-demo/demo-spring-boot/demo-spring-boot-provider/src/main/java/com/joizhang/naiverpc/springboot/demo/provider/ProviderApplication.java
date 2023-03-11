package com.joizhang.naiverpc.springboot.demo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

@SpringBootApplication
public class ProviderApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ProviderApplication.class);
        System.out.println("naive-rpc service started");
        new CountDownLatch(1).await();
    }

}
