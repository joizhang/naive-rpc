package com.joizhang.naiverpc.demo;

import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.demo.api.dto.User;
import com.joizhang.naiverpc.demo.api.service.HelloService;
import com.joizhang.naiverpc.demo.api.service.UserService;
import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.NettyRpcAccessPoint;
import com.joizhang.naiverpc.spi.ServiceSupport;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Objects;

@Slf4j
public class DemoClientApp {

    private static final ServiceSupport<RpcAccessPoint> RPC_ACCESS_POINT_SERVICE_SUPPORT =
            ServiceSupport.getServiceSupport(RpcAccessPoint.class);

    static <T> T lookupService(RpcAccessPoint rpcAccessPoint, NameService nameService, Class<T> serviceClass)
            throws Exception {
        Objects.requireNonNull(nameService);
        String serviceName = serviceClass.getCanonicalName();
        InetSocketAddress socketAddress = nameService.lookupService(serviceName);
        log.info("找到服务: {}，提供者: {}.", serviceName, socketAddress);
        return rpcAccessPoint.getRemoteService(nameService, serviceClass);
    }

    public static void main(String[] args) throws Exception {
        try (RpcAccessPoint rpcAccessPoint = RPC_ACCESS_POINT_SERVICE_SUPPORT.getService(
                NettyRpcAccessPoint.class.getCanonicalName())) {
            NameService nameService = rpcAccessPoint.getNameService(DemoClientApp.class);

            HelloService helloService = DemoClientApp.lookupService(rpcAccessPoint, nameService, HelloService.class);
            String response = helloService.hello("joizhang");
            System.out.println(response);

            UserService userService = DemoClientApp.lookupService(rpcAccessPoint, nameService, UserService.class);
            User user = userService.incrementVersion(new User("joizhang", (byte) 1, (short) 30, 1));
            System.out.println(user);
        }
    }

}
