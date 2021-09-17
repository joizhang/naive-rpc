package com.joizhang.naiverpc.remoting;

import com.joizhang.naiverpc.remoting.transport.RequestHandlerRegistry;
import com.joizhang.naiverpc.spi.SPI;

@SPI
public interface TransportServer {

    void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception;

    void stop();

}
