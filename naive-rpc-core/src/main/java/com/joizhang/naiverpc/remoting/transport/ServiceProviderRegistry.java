package com.joizhang.naiverpc.remoting.transport;

/**
 * 服务提供者注册类
 */
public interface ServiceProviderRegistry {

    /**
     * 注册服务
     *
     * @param serviceClass    服务的类型
     * @param serviceProvider 服务的实例
     */
    <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider);

}
