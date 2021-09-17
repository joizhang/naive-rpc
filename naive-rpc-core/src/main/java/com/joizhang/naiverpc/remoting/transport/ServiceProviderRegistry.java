package com.joizhang.naiverpc.remoting.transport;

import com.joizhang.naiverpc.spi.SPI;

@SPI
public interface ServiceProviderRegistry {

    <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider);

}
