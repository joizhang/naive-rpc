package com.joizhang.naiverpc.proxy;

import com.joizhang.naiverpc.nameservice.NameService;
import com.joizhang.naiverpc.remoting.client.TransportClient;
import com.joizhang.naiverpc.spi.SPI;

/**
 * 代理工厂
 */
@SPI
public interface StubFactory {

    /**
     * 创建代理实例
     *
     * @param <T>          服务类型
     * @param nameService  注册中心
     * @param serviceClass 服务名
     * @param client       通信客户端
     * @return 代理实例
     */
    <T> T createStub(NameService nameService, Class<T> serviceClass, TransportClient client);

}
