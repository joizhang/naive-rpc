package com.joizhang.naiverpc.netty.remoting.server;

import com.joizhang.naiverpc.netty.remoting.command.MessageType;
import com.joizhang.naiverpc.remoting.command.*;
import com.joizhang.naiverpc.remoting.server.RequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * RPC请求处理类
 */
@Slf4j
public class RpcRequestHandler implements RequestHandler {

    /**
     * {service name : service provider}
     */
    private final Map<String, Object> serviceProviders = new HashMap<>();

    @Override
    public byte type() {
        return MessageType.REQUEST_TYPE;
    }

    @Override
    public Command handle(@NotNull Command requestCommand) {
        Header requestHeader = requestCommand.getHeader();
        // 从payload中反序列化RpcRequest
        RpcRequest rpcRequest = (RpcRequest) requestCommand.getPayload();
        RpcResponse rpcResponse;
        try {
            // 查找所有已注册的服务提供方，寻找rpcRequest中需要的服务
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if (serviceProvider != null) {
                // 找到服务提供者，利用Java反射机制调用服务的对应方法
                String methodName = rpcRequest.getMethodName();
                Object[] args = rpcRequest.getArgs();
                Class<?>[] argsTypes = rpcRequest.getArgsTypes();
                Method method = serviceProvider.getClass().getMethod(methodName, argsTypes);
                Object result = method.invoke(serviceProvider, args);
                rpcResponse = RpcResponse.builder()
                        .code(ResponseCodeEnum.OK.getCode())
                        .body(result).build();
            } else {
                // 如果没找到，返回NO_PROVIDER错误响应。
                String error = MessageFormat.format("No service provider of {}#{}",
                        rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
                log.error(error);
                rpcResponse = RpcResponse.builder()
                        .code(ResponseCodeEnum.NOT_FOUND.getCode())
                        .error(error).build();
            }
        } catch (Throwable t) {
            // 发生异常，返回UNKNOWN_ERROR错误响应。
            log.error("Exception: ", t);
            rpcResponse = RpcResponse.builder()
                    .code(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode())
                    .error(t.getMessage()).build();
        }
        Header responseHeader = Header.builder()
                .rpcVersion(requestHeader.getRpcVersion())
                .messageType(MessageType.RESPONSE_TYPE)
                .codecType(requestHeader.getCodecType())
                .requestId(requestHeader.getRequestId())
                .build();
        return new Command(responseHeader, rpcResponse);
    }

    @Override
    public synchronized <T> void addServiceProvider(@NotNull Class<? extends T> serviceClass, T serviceProvider) {
        serviceProviders.put(serviceClass.getCanonicalName(), serviceProvider);
        log.debug("Add service: {}, provider: {}.",
                serviceClass.getCanonicalName(), serviceProvider.getClass().getCanonicalName());
    }
}
