package com.joizhang.naiverpc.netty.proxy;

import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.proxy.StubFactory;
import com.joizhang.naiverpc.remoting.client.Transport;
import com.joizhang.naiverpc.remoting.client.TransportClient;
import com.joizhang.naiverpc.remoting.command.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * 基于JDK动态代理的代理工厂
 */
public class JdkStubFactory implements StubFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(NameService nameService, Class<T> serviceClass, TransportClient client) {
        return (T) Proxy.newProxyInstance(
                serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass},
                new ProxyServiceStub<>(nameService, serviceClass, client)
        );
    }

    static class ProxyServiceStub<T> extends AbstractServiceStub implements InvocationHandler {

        private final NameService nameService;

        private final Class<?> serviceClass;

        public ProxyServiceStub(NameService nameService, Class<T> serviceClass, TransportClient client) {
            this.nameService = nameService;
            this.serviceClass = serviceClass;
            setTransportClient(client);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
            InetSocketAddress socketAddress = nameService.lookupService(serviceClass.getCanonicalName());
            Transport transport = clientMap.computeIfAbsent(socketAddress, this::createTransport);
            RpcRequest rpcRequest = RpcRequest.builder()
                    .interfaceName(this.serviceClass.getName())
                    .methodName(method.getName())
                    .args(args)
                    .argsTypes(method.getParameterTypes())
                    .build();
            return invokeRemote(transport, rpcRequest);
        }
    }
}
