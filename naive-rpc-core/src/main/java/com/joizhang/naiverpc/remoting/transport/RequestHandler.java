package com.joizhang.naiverpc.remoting.transport;


import com.joizhang.naiverpc.remoting.command.Command;
import com.joizhang.naiverpc.spi.SPI;

import java.io.IOException;

@SPI
public interface RequestHandler {

    /**
     * 处理请求
     *
     * @param requestCommand 请求命令
     * @return 响应命令
     */
    Command handle(Command requestCommand) throws IOException, ClassNotFoundException;

    /**
     * 支持的请求类型
     */
    byte type();

}
