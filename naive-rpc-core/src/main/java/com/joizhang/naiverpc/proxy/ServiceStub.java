package com.joizhang.naiverpc.proxy;

import com.joizhang.naiverpc.remoting.client.TransportClient;

public interface ServiceStub {

    void setTransportClient(TransportClient client);

}
