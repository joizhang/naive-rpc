package com.joizhang.naiverpc.remoting.server;

import com.joizhang.naiverpc.remoting.transport.RequestHandlerRegistry;
import com.joizhang.naiverpc.spi.SPI;

/**
 * 传输服务端
 */
@SPI
public interface TransportServer {

    /**
     * 启动服务端服务器
     *
     * @param requestHandlerRegistry 请求处理注册器
     * @param port                   端口
     */
    void start(RequestHandlerRegistry requestHandlerRegistry, int port);

    /**
     * 停止服务器
     */
    void stop();

}
