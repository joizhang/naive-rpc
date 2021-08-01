package com.joizhang.naiverpc.transport;

public interface TransportServer {

    void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception;

    void stop();

}
