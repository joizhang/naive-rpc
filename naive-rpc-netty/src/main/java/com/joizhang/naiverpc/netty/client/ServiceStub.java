package com.joizhang.naiverpc.netty.client;

import com.joizhang.naiverpc.transport.Transport;

public interface ServiceStub {

    void setTransport(Transport transport);

}
