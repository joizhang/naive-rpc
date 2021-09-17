package com.joizhang.naiverpc.netty.remoting.transport;

import com.joizhang.naiverpc.remoting.command.Command;
import com.joizhang.naiverpc.remoting.command.MessageType;
import com.joizhang.naiverpc.remoting.transport.RequestHandler;
import com.joizhang.naiverpc.remoting.transport.ServiceProviderRegistry;
import com.joizhang.naiverpc.spi.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class RpcRequestHandler implements RequestHandler, ServiceProviderRegistry {

    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);

    /**
     * {service name : service provider}
     */
    private final Map<String, Object> serviceProviders = new HashMap<>();

    @Override
    public Command handle(Command requestCommand) {
        return null;
        /*Header header = requestCommand.getHeader();
        // 从payload中反序列化RpcRequest
        RpcRequest rpcRequest = SerializeSupport.parse(requestCommand.getPayload());
        try {
            // 查找所有已注册的服务提供方，寻找rpcRequest中需要的服务
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if (serviceProvider != null) {
                // 找到服务提供者，利用Java反射机制调用服务的对应方法
                String arg = SerializeSupport.parse(rpcRequest.getArguments());
                Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), String.class);
                String result = (String) method.invoke(serviceProvider, arg);
                // 把结果封装成响应命令并返回
                return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId()), SerializeSupport.serialize(result));
            }
            // 如果没找到，返回NO_PROVIDER错误响应。
            logger.warn("No service Provider of {}#{}(String)!", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(), , header.getVersion(), header.getRequestId(), ResponseCodeEnum.NOT_FOUND.getCode(), "No provider!"), new byte[0]);
        } catch (Throwable t) {
            // 发生异常，返回UNKNOWN_ERROR错误响应。
            logger.error("Exception: ", t);
            return new Command(new ResponseHeader(type(), , header.getVersion(), header.getRequestId(), ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), t.getMessage()), new byte[0]);
        }*/
    }

    @Override
    public int type() {
        return MessageType.TYPE_RPC;
    }

    @Override
    public synchronized <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {
        serviceProviders.put(serviceClass.getCanonicalName(), serviceProvider);
        logger.info("Add service: {}, provider: {}.",
                serviceClass.getCanonicalName(), serviceProvider.getClass().getCanonicalName());
    }
}
