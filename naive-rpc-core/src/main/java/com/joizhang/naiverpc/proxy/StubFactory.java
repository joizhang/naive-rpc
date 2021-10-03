package com.joizhang.naiverpc.proxy;

import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.remoting.client.TransportClient;
import com.joizhang.naiverpc.spi.SPI;

import java.io.Closeable;

@SPI
public interface StubFactory {

    <T> T createStub(NameService nameService, TransportClient client, Class<T> serviceClass);

}
