package com.joizhang.naiverpc;

import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.spi.SPI;

import java.io.Closeable;
import java.net.URI;

@SPI
public interface RpcAccessPoint extends Closeable {

    /**
     * 服务端启动RPC框架，监听接口，开始提供远程服务。
     *
     * @return 服务实例，用于程序停止的时候安全关闭服务。
     */
    Closeable startServer(int port) throws Exception;

    /**
     * 获取注册中心的引用
     *
     * @return 注册中心引用
     */
    NameService getNameService();

    /**
     * 获取注册中心的引用
     *
     * @param nameServiceUri 注册中心 URI
     * @return 注册中心引用
     */
    NameService getNameService(URI nameServiceUri);

    /**
     * 服务端注册服务的实现实例
     *
     * @param service      实现实例
     * @param serviceClass 服务的接口类的Class
     * @param <T>          服务接口的类型
     */
    <T> void addServiceProvider(T service, Class<T> serviceClass);

    /**
     * 客户端获取远程服务的引用
     *
     * @param uri          远程服务地址
     * @param serviceClass 服务的接口类的Class
     * @param <T>          服务接口的类型
     * @return 远程服务引用
     */
    <T> T getRemoteService(URI uri, Class<T> serviceClass);

}
