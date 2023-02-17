package com.joizhang.naiverpc.remoting.client;

import com.joizhang.naiverpc.remoting.command.Command;
import com.joizhang.naiverpc.remoting.server.RequestHandler;

import java.util.concurrent.CompletableFuture;

/**
 * RPC请求传输类，该类发送的请求由 {@link RequestHandler} 处理
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
