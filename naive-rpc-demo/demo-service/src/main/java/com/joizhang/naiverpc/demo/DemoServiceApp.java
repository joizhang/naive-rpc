package com.joizhang.naiverpc.demo;

import com.joizhang.naiverpc.NameService;
import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.demo.api.service.HelloService;
import com.joizhang.naiverpc.demo.api.service.UserService;
import com.joizhang.naiverpc.demo.service.HelloServiceImpl;
import com.joizhang.naiverpc.demo.service.UserServiceImpl;
import com.joizhang.naiverpc.spi.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class DemoServiceApp {

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceApp.class);

    public static <T> void  registerService(RpcAccessPoint rpcAccessPoint, NameService nameService,
                                            Class<T> serviceClass, T service) throws IOException {
        String serviceName = serviceClass.getCanonicalName();
        URI uri = rpcAccessPoint.addServiceProvider(service, serviceClass);
        nameService.registerService(serviceName, uri);
    }

    public static void main(String[] args) throws Exception {
        logger.info("创建并启动 RpcAccessPoint...");
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
             Closeable ignored = rpcAccessPoint.startServer()) {
            NameService nameService = rpcAccessPoint.getNameService();
            Objects.requireNonNull(nameService);

            // register HelloService
            DemoServiceApp.registerService(rpcAccessPoint, nameService, HelloService.class, new HelloServiceImpl());
            // register UserService
            DemoServiceApp.registerService(rpcAccessPoint, nameService, UserService.class, new UserServiceImpl());

            nameService.displayMetaData();

            logger.info("开始提供服务，按任何键退出.");
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
            logger.info("Bye!");
        }
    }

}
