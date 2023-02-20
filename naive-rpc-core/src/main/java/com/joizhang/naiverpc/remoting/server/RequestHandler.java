package com.joizhang.naiverpc.remoting.server;


import com.joizhang.naiverpc.remoting.client.Transport;
import com.joizhang.naiverpc.remoting.command.Command;
import com.joizhang.naiverpc.remoting.transport.ServiceProviderRegistry;
import com.joizhang.naiverpc.spi.SPI;

import java.io.IOException;

/**
 * RPC请求处理类，该类用于处理 {@link Transport} 发送的请求
 */
@SPI
public interface RequestHandler extends ServiceProviderRegistry {

    /**
     * 支持的请求类型
     * @return 请求类型
     */
    byte type();

    /**
     * 处理请求
     *
     * @param requestCommand 请求命令
     * @return 响应命令
     */
    Command handle(Command requestCommand) throws IOException, ClassNotFoundException;

}
