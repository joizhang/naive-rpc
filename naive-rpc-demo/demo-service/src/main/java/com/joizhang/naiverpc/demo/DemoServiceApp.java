package com.joizhang.naiverpc.demo;

import com.joizhang.naiverpc.NameService;
import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.demo.api.service.HelloService;
import com.joizhang.naiverpc.demo.service.HelloServiceImpl;
import com.joizhang.naiverpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.net.URI;

public class DemoServiceApp {

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceApp.class);

    public static void main(String[] args) throws Exception {
        String serviceName = HelloService.class.getCanonicalName();
        HelloService helloService = new HelloServiceImpl();
        logger.info("创建并启动 RpcAccessPoint...");
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
             Closeable ignored = rpcAccessPoint.startServer()) {
            NameService nameService = rpcAccessPoint.getNameService();
            assert nameService != null;

            logger.info("向 RpcAccessPoint 注册 {} 服务...", serviceName);
            URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);

            logger.info("向 NameService 注册 {} 服务的地址 {} ...", serviceName, uri);
            nameService.registerService(serviceName, uri);

            logger.info("开始提供服务，按任何键退出.");
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
            logger.info("Bye!");
        }
    }

}
