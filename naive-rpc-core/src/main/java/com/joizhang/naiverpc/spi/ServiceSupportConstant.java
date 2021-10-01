package com.joizhang.naiverpc.spi;

import com.joizhang.naiverpc.loadbalance.LoadBalance;
import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.proxy.StubFactory;
import com.joizhang.naiverpc.remoting.TransportClient;
import com.joizhang.naiverpc.remoting.TransportServer;
import com.joizhang.naiverpc.remoting.transport.RequestHandler;
import com.joizhang.naiverpc.serialize.Serializer;

public interface ServiceSupportConstant {

    ServiceSupport<TransportServer> SERVER_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(TransportServer.class);
    ServiceSupport<TransportClient> CLIENT_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(TransportClient.class);
    ServiceSupport<NameService> NAME_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(NameService.class);
    ServiceSupport<StubFactory> STUB_FACTORY_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(StubFactory.class);
    ServiceSupport<Serializer> SERIALIZER_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(Serializer.class);
    ServiceSupport<RequestHandler> REQUEST_HANDLER_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(RequestHandler.class);
    ServiceSupport<LoadBalance> LOAD_BALANCE_SERVICE_SUPPORT = ServiceSupport.getServiceSupport(LoadBalance.class);

}
