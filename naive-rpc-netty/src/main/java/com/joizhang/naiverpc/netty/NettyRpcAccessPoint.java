package com.joizhang.naiverpc.netty;

import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.client.StubFactory;
import com.joizhang.naiverpc.spi.ServiceSupport;
import com.joizhang.naiverpc.transport.Transport;
import com.joizhang.naiverpc.server.ServiceProviderRegistry;
import com.joizhang.naiverpc.transport.RequestHandlerRegistry;
import com.joizhang.naiverpc.transport.TransportClient;
import com.joizhang.naiverpc.transport.TransportServer;

import java.io.Closeable;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class NettyRpcAccessPoint implements RpcAccessPoint {

    private URI uri;

    private TransportServer server = null;

    private TransportClient client = null;

    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();

    private final StubFactory stubFactory = ServiceSupport.load(StubFactory.class);

    private final ServiceProviderRegistry serviceProviderRegistry = ServiceSupport.load(ServiceProviderRegistry.class);

    @Override
    public synchronized Closeable startServer(int port) throws Exception {
        if (Objects.isNull(server)) {
            String host = Inet4Address.getLocalHost().getHostAddress();
            this.uri = URI.create("rpc://" + host + ":" + port);
            server = ServiceSupport.load(TransportServer.class);
            server.start(RequestHandlerRegistry.getInstance(), port);
        }
        return () -> {
            if(server != null) {
                server.stop();
            }
        };
    }

    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        return stubFactory.createStub(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {
        try {
            client = ServiceSupport.load(TransportClient.class);
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()),30000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(serviceClass, service);
        return uri;
    }

    @Override
    public void close() {
        if(server != null) {
            server.stop();
        }
        if (client != null) {
            client.close();
        }
    }

}
