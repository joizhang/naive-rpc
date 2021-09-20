package com.joizhang.naiverpc.spi;

import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.proxy.StubFactory;
import com.joizhang.naiverpc.remoting.TransportClient;
import com.joizhang.naiverpc.remoting.TransportServer;
import com.joizhang.naiverpc.remoting.transport.ServiceProviderRegistry;
import com.joizhang.naiverpc.serialize.Serializer;

public interface ServiceSupportConstant {

    ServiceSupport<TransportServer> SERVER_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(TransportServer.class);
    ServiceSupport<TransportClient> CLIENT_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(TransportClient.class);
    ServiceSupport<NameService> NAME_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(NameService.class);
    ServiceSupport<StubFactory> STUB_FACTORY_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(StubFactory.class);
    ServiceSupport<ServiceProviderRegistry> SERVICE_PROVIDER_REGISTRY_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(ServiceProviderRegistry.class);
    ServiceSupport<Serializer> SERIALIZER_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(Serializer.class);

}
