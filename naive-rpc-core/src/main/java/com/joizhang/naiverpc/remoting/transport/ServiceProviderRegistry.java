package com.joizhang.naiverpc.remoting.transport;

public interface ServiceProviderRegistry {

    <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider);

}
