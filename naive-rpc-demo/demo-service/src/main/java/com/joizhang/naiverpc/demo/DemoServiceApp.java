package com.joizhang.naiverpc.demo;

import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.demo.api.service.HelloService;
import com.joizhang.naiverpc.demo.api.service.UserService;
import com.joizhang.naiverpc.demo.service.HelloServiceImpl;
import com.joizhang.naiverpc.demo.service.UserServiceImpl;
import com.joizhang.naiverpc.spi.ServiceSupport;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

public class DemoServiceApp {

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceApp.class);

    public static void main(String[] args) throws Exception {
        CommandLine parser = parseArgs(args);
        String host = parser.getOptionValue("host", "localhost");
        int port = Integer.parseInt(parser.getOptionValue("port", "9999"));
        logger.info("创建并启动 RpcAccessPoint...");
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
             Closeable ignored = rpcAccessPoint.startServer(host, port)) {
            NameService nameService = rpcAccessPoint.getNameService();

            //register HelloService
            DemoServiceApp.registerService(rpcAccessPoint, nameService, HelloService.class, new HelloServiceImpl());
            //register UserService
            DemoServiceApp.registerService(rpcAccessPoint, nameService, UserService.class, new UserServiceImpl());
            nameService.displayMetaData();

            logger.info("开始提供服务，按任何键退出.");
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
            logger.info("Bye!");
        }
    }

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

    private static <T> void  registerService(RpcAccessPoint rpcAccessPoint, NameService nameService,
                                             Class<T> serviceClass, T service) throws IOException {
        Objects.requireNonNull(nameService);
        String serviceName = serviceClass.getCanonicalName();
        URI uri = rpcAccessPoint.addServiceProvider(service, serviceClass);
        nameService.registerService(serviceName, uri);
    }

}
