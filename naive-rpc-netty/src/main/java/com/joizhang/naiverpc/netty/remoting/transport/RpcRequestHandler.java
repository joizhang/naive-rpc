package com.joizhang.naiverpc.netty.remoting.transport;

import com.joizhang.naiverpc.netty.remoting.command.CodecTypeEnum;
import com.joizhang.naiverpc.netty.remoting.command.MessageType;
import com.joizhang.naiverpc.netty.serialize.SerializeSupport;
import com.joizhang.naiverpc.remoting.command.*;
import com.joizhang.naiverpc.remoting.transport.RequestHandler;
import com.joizhang.naiverpc.remoting.transport.ServiceProviderRegistry;
import com.joizhang.naiverpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.SERIALIZER_SERVICE_SUPPORT;

@Slf4j
public class RpcRequestHandler implements RequestHandler, ServiceProviderRegistry {

    /**
     * {service name : service provider}
     */
    private final Map<String, Object> serviceProviders = new HashMap<>();

    @Override
    public Command handle(Command requestCommand) throws IOException, ClassNotFoundException {
        Header header = requestCommand.getHeader();
        // 从payload中反序列化RpcRequest
        String codecName = CodecTypeEnum.getName(header.getCodecType());
        Serializer serializer = SERIALIZER_SERVICE_SUPPORT.getService(codecName);
        byte[] requestPayload = (byte[]) requestCommand.getPayload();
        RpcRequest rpcRequest = SerializeSupport.deserialize(serializer, requestPayload, RpcRequest.class);
        ResponseHeader responseHeader;
        Object resultPayload;
        try {
            // 查找所有已注册的服务提供方，寻找rpcRequest中需要的服务
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if (serviceProvider != null) {
                // 找到服务提供者，利用Java反射机制调用服务的对应方法
                String methodName = rpcRequest.getMethodName();
                Object[] args = rpcRequest.getArgs();
                Method method = serviceProvider.getClass().getMethod(methodName, String.class);
                resultPayload = method.invoke(serviceProvider, args);
                responseHeader = new ResponseHeader(header.getRpcVersion(), type(),
                        header.getCodecType(), header.getRequestId());
            } else {
                // 如果没找到，返回NO_PROVIDER错误响应。
                log.error("No service provider of {}#{}!", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
                resultPayload = new byte[0];
                responseHeader = new ResponseHeader(header.getRpcVersion(), type(),
                        header.getCodecType(), header.getRequestId(),
                        ResponseCodeEnum.NOT_FOUND.getCode(), "No provider!");
            }
        } catch (Throwable t) {
            // 发生异常，返回UNKNOWN_ERROR错误响应。
            log.error("Exception: ", t);
            resultPayload = new byte[0];
            responseHeader = new ResponseHeader(header.getRpcVersion(), type(),
                    header.getCodecType(),  header.getRequestId(),
                    ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), t.getMessage());
        }
        return new Command(responseHeader, resultPayload);
    }

    @Override
    public byte type() {
        return MessageType.TYPE_RPC;
    }

    @Override
    public synchronized <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {
        serviceProviders.put(serviceClass.getCanonicalName(), serviceProvider);
        log.info("Add service: {}, provider: {}.",
                serviceClass.getCanonicalName(), serviceProvider.getClass().getCanonicalName());
    }
}
