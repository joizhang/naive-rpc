package com.joizhang.naiverpc.nameservice;

import com.joizhang.naiverpc.spi.SPI;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collection;

/**
 * 注册中心
 */
@SPI
public interface NameService extends Closeable {

    /**
     * 所有支持的协议
     */
    Collection<String> supportedSchemes();

    /**
     * 连接注册中心
     *
     * @param nameServiceUri 注册中心地址
     */
    void connect(URI nameServiceUri);

    /**
     * 注册服务
     *
     * @param serviceName 服务名称
     */
    void registerService(InetSocketAddress socketAddress, String serviceName) throws Exception;

    /**
     * 查询服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    InetSocketAddress lookupService(String serviceName) throws Exception;

    /**
     * 输出服务元信息
     */
    void displayMetaData();

}
