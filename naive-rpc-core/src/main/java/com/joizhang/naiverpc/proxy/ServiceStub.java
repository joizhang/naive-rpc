package com.joizhang.naiverpc.proxy;

import com.joizhang.naiverpc.remoting.client.Transport;
import com.joizhang.naiverpc.remoting.client.TransportClient;
import com.joizhang.naiverpc.remoting.command.RpcRequest;

import java.net.InetSocketAddress;

/**
 * 代理服务实现
 */
public interface ServiceStub {

    /**
     * 设置通信客户端
     *
     * @param client 通信客户端
     */
    void setTransportClient(TransportClient client);

    /**
     * 新建传输对象
     *
     * @param socketAddress 服务地址
     * @return 传输对象
     */
    Transport createTransport(InetSocketAddress socketAddress);

    /**
     * 远程过程调用
     *
     * @param transport 传输对象
     * @param request   请求
     * @return 请求结果，也是服务调用返回的结果
     */
    Object invokeRemote(Transport transport, RpcRequest request);

}
