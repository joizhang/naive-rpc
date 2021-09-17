package com.joizhang.naiverpc.netty;

import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.remoting.server.NettyServer;
import com.joizhang.naiverpc.proxy.StubFactory;
import com.joizhang.naiverpc.remoting.Transport;
import com.joizhang.naiverpc.remoting.TransportClient;
import com.joizhang.naiverpc.remoting.TransportServer;
import com.joizhang.naiverpc.remoting.transport.RequestHandlerRegistry;
import com.joizhang.naiverpc.remoting.transport.ServiceProviderRegistry;
import com.joizhang.naiverpc.spi.ServiceSupport;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class NettyRpcAccessPoint implements RpcAccessPoint {

    private static final ServiceSupport<TransportServer> SERVER_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(TransportServer.class);
    private static final ServiceSupport<TransportClient> CLIENT_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(TransportClient.class);
    private static final ServiceSupport<NameService> NAME_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(NameService.class);
    private static final ServiceSupport<StubFactory> STUB_FACTORY_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(StubFactory.class);
    private static final ServiceSupport<ServiceProviderRegistry> SERVICE_PROVIDER_REGISTRY_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(ServiceProviderRegistry.class);

    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();

    private URI uri;

    private TransportServer server = null;

    private TransportClient client = null;

    @Override
    public synchronized Closeable startServer(String host, int port) throws Exception {
        if (Objects.isNull(server)) {
            this.uri = URI.create("rpc://" + host + ":" + port);
            this.server = SERVER_SERVICE_SUPPORT.getService(NettyServer.class.getCanonicalName());
            this.server.start(RequestHandlerRegistry.getInstance(), port);
        }
        return () -> {
            if (this.server != null) {
                this.server.stop();
            }
        };
    }

    @Override
    public NameService getNameService(URI nameServiceUri) {
        Collection<NameService> nameServices = NAME_SERVICE_SUPPORT.getAllService();
        for (NameService nameService : nameServices) {
            if (nameService.supportedSchemes().contains(nameServiceUri.getScheme())) {
                nameService.connect(nameServiceUri);
                return nameService;
            }
        }
        return null;
    }

    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = this.clientMap.computeIfAbsent(uri, this::createTransport);
        final StubFactory stubFactory = STUB_FACTORY_SERVICE_SUPPORT.getService(StubFactory.class.getCanonicalName());
        return stubFactory.createStub(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {
        try {
            this.client = CLIENT_SERVICE_SUPPORT.getService(TransportClient.class.getCanonicalName());
            InetSocketAddress address = new InetSocketAddress(uri.getHost(), uri.getPort());
            return this.client.createTransport(address, 3000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        final ServiceProviderRegistry serviceProviderRegistry = SERVICE_PROVIDER_REGISTRY_SERVICE_SUPPORT.getService(ServiceProviderRegistry.class.getCanonicalName());
        serviceProviderRegistry.addServiceProvider(serviceClass, service);
        return this.uri;
    }

    @Override
    public void close() {
        if (this.server != null) {
            this.server.stop();
        }
        if (this.client != null) {
            this.client.close();
        }
    }

}
