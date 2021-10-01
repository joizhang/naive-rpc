package com.joizhang.naiverpc.netty;

import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.proxy.JdkStubFactory;
import com.joizhang.naiverpc.netty.remoting.client.NettyClient;
import com.joizhang.naiverpc.netty.remoting.server.NettyServer;
import com.joizhang.naiverpc.netty.remoting.transport.RpcRequestHandler;
import com.joizhang.naiverpc.proxy.StubFactory;
import com.joizhang.naiverpc.remoting.client.Transport;
import com.joizhang.naiverpc.remoting.client.TransportClient;
import com.joizhang.naiverpc.remoting.server.TransportServer;
import com.joizhang.naiverpc.remoting.transport.RequestHandlerRegistry;
import com.joizhang.naiverpc.remoting.transport.ServiceProviderRegistry;
import com.joizhang.naiverpc.utils.NaiveRpcPropertiesSingleton;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.*;

public class NettyRpcAccessPoint implements RpcAccessPoint {

    public static final NaiveRpcPropertiesSingleton PROPERTIES_SINGLETON = NaiveRpcPropertiesSingleton.getInstance();

    public static final String REGISTER_ADDRESS = "naive.register.address";

    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();

    private TransportServer server = null;

    private TransportClient client = null;

    @Override
    public synchronized Closeable startServer(int port) throws Exception {
        if (Objects.isNull(server)) {
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
    public NameService getNameService() {
        URI uri;
        String registerAddress = PROPERTIES_SINGLETON.getStringValue(REGISTER_ADDRESS);
        if (Objects.isNull(registerAddress)) {
            uri = URI.create("file://localhost:9999");

        } else {
            try {
                uri = new URI(registerAddress);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Illegal uri syntax of naive.register.address in application.properties.");
            }
        }
        return getNameService(uri);
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
        StubFactory stubFactory = STUB_FACTORY_SERVICE_SUPPORT.getService(JdkStubFactory.class.getCanonicalName());
        return stubFactory.createStub(transport, serviceClass);
    }

    private Transport createTransport(URI uri) {
        try {
            this.client = CLIENT_SERVICE_SUPPORT.getService(NettyClient.class.getCanonicalName());
            InetSocketAddress address = new InetSocketAddress(uri.getHost(), uri.getPort());
            return this.client.createTransport(address, 3000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized <T> void addServiceProvider(T service, Class<T> serviceClass) {
        ServiceProviderRegistry registry = REQUEST_HANDLER_SERVICE_SUPPORT.getService(RpcRequestHandler.class.getCanonicalName());
        registry.addServiceProvider(serviceClass, service);
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
