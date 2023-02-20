package com.joizhang.naiverpc.remoting.client;

import com.joizhang.naiverpc.spi.SPI;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 传输客户端，用于新建传输对象
 */
@SPI
public interface TransportClient extends Closeable {

    /**
     * 新建传输对象
     *
     * @param address           服务地址
     * @param connectionTimeout 超时时间
     * @return 传输对象
     * @throws InterruptedException InterruptedException
     * @throws TimeoutException     TimeoutException
     */
    Transport createTransport(SocketAddress address, long connectionTimeout)
            throws InterruptedException, TimeoutException;

    /**
     * 新建传输对象
     *
     * @param address           服务地址
     * @param connectionTimeout 超时时间
     * @param timeUnit          超时时间单位
     * @return 传输对象
     * @throws InterruptedException InterruptedException
     * @throws TimeoutException     TimeoutException
     */
    Transport createTransport(SocketAddress address, long connectionTimeout, TimeUnit timeUnit)
            throws InterruptedException, TimeoutException;

    /**
     * 关闭连接
     */
    @Override
    void close();

}
