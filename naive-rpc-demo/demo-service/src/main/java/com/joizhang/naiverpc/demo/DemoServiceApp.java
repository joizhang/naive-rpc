package com.joizhang.naiverpc.demo;

import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.demo.api.service.HelloService;
import com.joizhang.naiverpc.demo.api.service.UserService;
import com.joizhang.naiverpc.demo.service.HelloServiceImpl;
import com.joizhang.naiverpc.demo.service.UserServiceImpl;
import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.NettyRpcAccessPoint;
import com.joizhang.naiverpc.spi.ServiceSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.net.InetSocketAddress;
import java.util.Objects;

@Slf4j
public class DemoServiceApp {

    private static final ServiceSupport<RpcAccessPoint> RPC_ACCESS_POINT_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(RpcAccessPoint.class);

    private static CommandLine parseArgs(String[] args) {
        Options options = new Options();
        Option host = new Option("h", "host", true, "host");
        host.setRequired(false);
        host.setType(String.class);
        options.addOption(host);

        Option port = new Option("p", "port", true, "port");
        port.setRequired(false);
        host.setType(Integer.class);
        options.addOption(port);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }
        return cmd;
    }

    private static <T> void registerService(RpcAccessPoint rpcAccessPoint, NameService nameService,
                                            InetSocketAddress socketAddress, Class<T> serviceClass, T service)
            throws Exception {
        Objects.requireNonNull(nameService);
        String serviceName = serviceClass.getCanonicalName();
        rpcAccessPoint.addServiceProvider(serviceClass, service);
        nameService.registerService(socketAddress, serviceName);
    }

    public static void main(String[] args) throws Exception {
        log.info("创建并启动 RpcAccessPoint...");
        try (RpcAccessPoint rpcAccessPoint = RPC_ACCESS_POINT_SERVICE_SUPPORT.getService(NettyRpcAccessPoint.class.getCanonicalName());
             NameService nameService = rpcAccessPoint.getNameService(DemoServiceApp.class)) {
            InetSocketAddress socketAddress = rpcAccessPoint.startServer();

            //register HelloService
            DemoServiceApp.registerService(rpcAccessPoint, nameService, socketAddress, HelloService.class, new HelloServiceImpl());
            //register UserService
            DemoServiceApp.registerService(rpcAccessPoint, nameService, socketAddress, UserService.class, new UserServiceImpl());
            nameService.displayMetaData();

            log.info("开始提供服务，按任何键退出.");
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
            log.info("Bye!");
        }
    }

}
