package com.joizhang.naiverpc.demo;

import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.demo.api.service.HelloService;
import com.joizhang.naiverpc.demo.api.service.UserService;
import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.NettyRpcAccessPoint;
import com.joizhang.naiverpc.spi.ServiceSupport;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.concurrent.*;

@Slf4j
public class DemoThreadPoolClientApp {

    private static final ServiceSupport<RpcAccessPoint> RPC_ACCESS_POINT_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(RpcAccessPoint.class);

    private static <T> T lookupService(RpcAccessPoint rpcAccessPoint, NameService nameService, Class<T> serviceClass)
            throws IOException, ClassNotFoundException {
        Objects.requireNonNull(nameService);
        String serviceName = serviceClass.getCanonicalName();
        URI uri = nameService.lookupService(serviceName);
        log.info("找到服务{}，提供者: {}.", serviceName, uri);
        return rpcAccessPoint.getRemoteService(uri, serviceClass);
    }

    public static void main(String[] args) throws IOException {
        ExecutorService service = new ThreadPoolExecutor(
                4,
                4,
                0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(200),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());

        try (RpcAccessPoint rpcAccessPoint = RPC_ACCESS_POINT_SERVICE_SUPPORT.getService(NettyRpcAccessPoint.class.getCanonicalName())) {
            NameService nameService = rpcAccessPoint.getNameService();
            HelloService helloService = DemoClientApp.lookupService(rpcAccessPoint, nameService, HelloService.class);
            UserService userService = DemoClientApp.lookupService(rpcAccessPoint, nameService, UserService.class);

            int loopTime = 100;
            CountDownLatch latch1 = new CountDownLatch(loopTime);
            for (int i = 0; i < loopTime; i++) {
                final int seq = i;
                service.execute(()-> {
                    String response = helloService.hello("joizhang " + seq);
                    log.info("收到响应: {}.", response);
                    latch1.countDown();
                });
            }
            latch1.await();

//            CountDownLatch latch2 = new CountDownLatch(loopTime);
//            for (int i = 0; i < loopTime; i++) {
//                final int seq = i;
//                service.execute(()-> {
//                    User user = new User();
//                    user.setUsername("joizhang");
//                    user.setAge((short) 28);
//                    user.setGender((byte) 1);
//                    user.setVersion(seq);
//                    User response = userService.incrementVersion(user);
//                    logger.info("收到响应: {}.", response);
//                    latch2.countDown();
//                });
//            }
//            latch2.await();

            service.shutdown();
        } catch (InterruptedException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}