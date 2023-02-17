package com.joizhang.naiverpc.remoting.client;

import com.joizhang.naiverpc.remoting.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 传输对象
 */
public interface Transport {

    /**
     * 发送请求命令
     *
     * @param request 请求命令
     * @return 返回值是一个Future，Future
     */
    CompletableFuture<Command> send(Command request);

}
