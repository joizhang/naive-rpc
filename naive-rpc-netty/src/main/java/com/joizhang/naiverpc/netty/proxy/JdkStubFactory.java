package com.joizhang.naiverpc.netty.proxy;

import com.joizhang.naiverpc.proxy.StubFactory;
import com.joizhang.naiverpc.remoting.command.RpcRequest;
import com.joizhang.naiverpc.remoting.Transport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkStubFactory implements StubFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass}, new ProxyStub<>(transport, serviceClass));
    }

    static class ProxyStub<T> extends AbstractStub implements InvocationHandler {

        private final Class<?> serviceClass;

        public ProxyStub(Transport transport, Class<T> serviceClass) {
            this.transport = transport;
            this.serviceClass = serviceClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            RpcRequest rpcRequest = RpcRequest.builder()
                    .interfaceName(this.serviceClass.getName())
                    .methodName(method.getName())
                    .args(args)
                    .argsTypes(method.getParameterTypes())
                    .build();
            return invokeRemote(rpcRequest);
        }
    }
}
