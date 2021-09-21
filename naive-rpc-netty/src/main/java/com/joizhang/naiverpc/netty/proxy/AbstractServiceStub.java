package com.joizhang.naiverpc.netty.proxy;

import com.joizhang.naiverpc.netty.remoting.command.CodecTypeEnum;
import com.joizhang.naiverpc.netty.remoting.command.MessageType;
import com.joizhang.naiverpc.netty.remoting.command.RpcConstants;
import com.joizhang.naiverpc.proxy.ServiceStub;
import com.joizhang.naiverpc.remoting.Transport;
import com.joizhang.naiverpc.remoting.command.*;

import java.util.concurrent.ExecutionException;

public abstract class AbstractServiceStub implements ServiceStub {

    protected Transport transport;

    protected Object invokeRemote(RpcRequest request) {
        Header requestHeader = Header.builder()
                .rpcVersion(RpcConstants.RPC_VERSION)
                .messageType(MessageType.REQUEST_TYPE)
                .codecType(CodecTypeEnum.JAVA.getCode())
                .requestId(RequestIdSupport.next()).build();
        Command requestCommand = new Command(requestHeader, request);
        try {
            Command responseCommand = transport.send(requestCommand).get();
            RpcResponse rpcResponse = (RpcResponse) responseCommand.getPayload();
            if (rpcResponse.getCode() == ResponseCodeEnum.OK.getCode()) {
                return responseCommand.getPayload();
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
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

}
