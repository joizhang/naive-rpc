package com.joizhang.naiverpc.proxy;

import com.joizhang.naiverpc.remoting.client.Transport;

public interface ServiceStub {

    void setTransport(Transport transport);

}
