package com.joizhang.naiverpc.netty;

import com.joizhang.naiverpc.RpcAccessPoint;
import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.netty.proxy.JdkStubFactory;
import com.joizhang.naiverpc.netty.remoting.client.NettyClient;
import com.joizhang.naiverpc.netty.remoting.server.NettyServer;
import com.joizhang.naiverpc.netty.remoting.server.RpcRequestHandler;
import com.joizhang.naiverpc.utils.NetUtils;
import com.joizhang.naiverpc.utils.StringUtils;
import com.joizhang.naiverpc.proxy.StubFactory;
import com.joizhang.naiverpc.remoting.client.TransportClient;
import com.joizhang.naiverpc.remoting.server.TransportServer;
import com.joizhang.naiverpc.remoting.transport.RequestHandlerRegistry;
import com.joizhang.naiverpc.remoting.transport.ServiceProviderRegistry;
import com.joizhang.naiverpc.config.NaiveRpcPropertiesSingleton;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.Objects;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.*;

public class NettyRpcAccessPoint implements RpcAccessPoint {

    public static final NaiveRpcPropertiesSingleton PROPERTIES_SINGLETON = NaiveRpcPropertiesSingleton.getInstance();

    public static final String REGISTER_ADDRESS = "naiverpc.register.address";

    public static final String SERVICE_DATA = "simple_rpc_name_service.data";

    private TransportServer server;

    private TransportClient client;

    @Override
    public synchronized InetSocketAddress startServer() throws Exception {
        String hostToBind = InetAddress.getLocalHost().getHostAddress();
        int port = NetUtils.getRandomPort();
        String registerAddress = PROPERTIES_SINGLETON.getStringValue(REGISTER_ADDRESS);
        if (StringUtils.isEmpty(registerAddress)) {
            port = NetUtils.getAvailablePort(9999);
        }
        if (Objects.isNull(server)) {
            this.server = SERVER_SERVICE_SUPPORT.getService(NettyServer.class.getCanonicalName());
            this.server.start(RequestHandlerRegistry.getInstance(), port);
        }
        return new InetSocketAddress(hostToBind, port);
    }

    @Override
    public <T> NameService getNameService(Class<T> primarySource) {
        URI nameServiceUri;
        String registerAddress = PROPERTIES_SINGLETON.getStringValue(REGISTER_ADDRESS);
        if (StringUtils.isEmpty(registerAddress)) {
            // 未指定注册中心，默认为本地注册
            URL path = primarySource.getProtectionDomain().getCodeSource().getLocation();
            File classpath = new File(path.getPath());
            File parentFile = classpath.getParentFile();
            nameServiceUri = new File(parentFile, SERVICE_DATA).toURI();
        } else {
            nameServiceUri = URI.create(registerAddress);
        }
        return getNameService(nameServiceUri);
    }

    private NameService getNameService(URI nameServiceUri) {
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
    public synchronized <T> void addServiceProvider(Class<T> serviceClass, T service) {
        ServiceProviderRegistry registry = REQUEST_HANDLER_SERVICE_SUPPORT.getService(
                RpcRequestHandler.class.getCanonicalName());
        registry.addServiceProvider(serviceClass, service);
    }

    @Override
    public <T> T getRemoteService(NameService nameService, Class<T> serviceClass) {
        this.client = CLIENT_SERVICE_SUPPORT.getService(NettyClient.class.getCanonicalName());
        StubFactory stubFactory = STUB_FACTORY_SERVICE_SUPPORT.getService(
                JdkStubFactory.class.getCanonicalName());
        return stubFactory.createStub(nameService, serviceClass, this.client);
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
