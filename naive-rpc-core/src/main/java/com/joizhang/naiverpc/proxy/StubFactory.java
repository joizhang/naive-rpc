package com.joizhang.naiverpc.proxy;

import com.joizhang.naiverpc.remoting.client.Transport;
import com.joizhang.naiverpc.spi.SPI;

@SPI
public interface StubFactory {

    <T> T createStub(Transport transport, Class<T> serviceClass);

}
