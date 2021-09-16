package com.joizhang.naiverpc.remoting;

import com.joizhang.naiverpc.remoting.transport.RequestHandlerRegistry;

public interface TransportServer {

    void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception;

    void stop();

}
