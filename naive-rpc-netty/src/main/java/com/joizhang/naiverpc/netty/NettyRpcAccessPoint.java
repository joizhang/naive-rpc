package com.joizhang.naiverpc.netty;

import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.proxy.StubFactory;
import com.joizhang.naiverpc.spi.ServiceSupport;
import com.joizhang.naiverpc.remoting.Transport;
import com.joizhang.naiverpc.remoting.transport.ServiceProviderRegistry;
import com.joizhang.naiverpc.remoting.transport.RequestHandlerRegistry;
import com.joizhang.naiverpc.remoting.TransportClient;
import com.joizhang.naiverpc.remoting.TransportServer;
import com.joizhang.naiverpc.spi.Singleton;

import java.io.Closeable;
import java.net.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Singleton
public class NettyRpcAccessPoint implements RpcAccessPoint {

    private URI uri;

    private TransportServer server = null;

    private TransportClient client = null;

    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();

    private final StubFactory stubFactory = ServiceSupport.load(StubFactory.class);

    private final ServiceProviderRegistry serviceProviderRegistry = ServiceSupport.load(ServiceProviderRegistry.class);

    @Override
    public synchronized Closeable startServer(String host, int port) throws Exception {
        if (Objects.isNull(server)) {
            this.uri = URI.create("rpc://" + host + ":" + port);
            this.server = ServiceSupport.load(TransportServer.class);
            this.server.start(RequestHandlerRegistry.getInstance(), port);
        }
        return () -> {
            if(this.server != null) {
                this.server.stop();
            }
        };
    }

    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = this.clientMap.computeIfAbsent(uri, this::createTransport);
        return this.stubFactory.createStub(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {
        try {
            this.client = ServiceSupport.load(TransportClient.class);
            InetSocketAddress address = new InetSocketAddress(uri.getHost(), uri.getPort());
            return this.client.createTransport(address,3000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        this.serviceProviderRegistry.addServiceProvider(serviceClass, service);
        return this.uri;
    }

    @Override
    public void close() {
        if(this.server != null) {
            this.server.stop();
        }
        if (this.client != null) {
            this.client.close();
        }
    }

}
