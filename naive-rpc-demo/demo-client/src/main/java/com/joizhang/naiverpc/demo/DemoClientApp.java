package com.joizhang.naiverpc.demo;

import com.joizhang.naiverpc.NameService;
import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.demo.api.service.HelloService;
import com.joizhang.naiverpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.*;

public class DemoClientApp {

    private static final Logger logger = LoggerFactory.getLogger(DemoClientApp.class);

    private static final ExecutorService SERVICE = new ThreadPoolExecutor(
            4,
            4,
            0, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(50),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardPolicy());

    public static void main(String[] args) throws IOException {
        String serviceName = HelloService.class.getCanonicalName();
        String name = "joizhang";
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
            NameService nameService = rpcAccessPoint.getNameService();
            assert nameService != null;
            URI uri = nameService.lookupService(serviceName);
            assert uri != null;
            logger.info("找到服务{}，提供者: {}.", serviceName, uri);
            HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);

            String response = helloService.hello(name);
            logger.info("收到响应: {}.", response);
//            SERVICE.submit(()-> {
//                // logger.info("请求服务, name: {}...", name);
//                String response = helloService.hello(name);
//                logger.info("收到响应: {}.", response);
//            });

//            for (int i = 0; i < 10; i++) {
//                String response = helloService.hello(name);
//                logger.info("收到响应: {}.", response);
//                SERVICE.submit(()-> {
//                    // logger.info("请求服务, name: {}...", name);
//                    String response = helloService.hello(name);
//                    logger.info("收到响应: {}.", response);
//                });
//            }

        }
    }

}
