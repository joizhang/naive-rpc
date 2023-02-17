package com.joizhang.naiverpc.netty.proxy;

import com.joizhang.naiverpc.netty.remoting.command.CodecTypeEnum;
import com.joizhang.naiverpc.netty.remoting.command.MessageType;
import com.joizhang.naiverpc.netty.remoting.command.RpcConstants;
import com.joizhang.naiverpc.proxy.ServiceStub;
import com.joizhang.naiverpc.remoting.client.Transport;
import com.joizhang.naiverpc.remoting.client.TransportClient;
import com.joizhang.naiverpc.remoting.command.*;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractServiceStub implements ServiceStub {

    protected final Map<InetSocketAddress, Transport> clientMap = new ConcurrentHashMap<>();

    private TransportClient client;

    @Override
    public Object invokeRemote(Transport transport, RpcRequest request) {
        Header requestHeader = Header.builder()
                .rpcVersion(RpcConstants.RPC_VERSION)
                .messageType(MessageType.REQUEST_TYPE)
                .codecType(CodecTypeEnum.JAVA.getCode()) // TODO get codec from properties file
                .requestId(RequestIdSupport.next()).build();
        Command requestCommand = new Command(requestHeader, request);
        try {
            Command responseCommand = transport.send(requestCommand).get();
            RpcResponse rpcResponse = (RpcResponse) responseCommand.getPayload();
            if (rpcResponse.getCode() == ResponseCodeEnum.OK.getCode()) {
                return rpcResponse.getBody();
            } else {
                throw new Exception(rpcResponse.getError());
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transport createTransport(InetSocketAddress socketAddress) {
        try {
            return client.createTransport(socketAddress, 3000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setTransportClient(TransportClient client) {
        this.client = client;
    }

}
