package com.joizhang.naiverpc.netty.proxy;

import com.joizhang.naiverpc.netty.remoting.command.CodecTypeEnum;
import com.joizhang.naiverpc.netty.remoting.command.MessageType;
import com.joizhang.naiverpc.proxy.ServiceStub;
import com.joizhang.naiverpc.remoting.Transport;
import com.joizhang.naiverpc.remoting.command.*;

import java.util.concurrent.ExecutionException;

public abstract class AbstractServiceStub implements ServiceStub {

    protected Transport transport;

    protected Object invokeRemote(RpcRequest request) {
        Header header = new Header((byte) 1, MessageType.TYPE_RPC, CodecTypeEnum.JAVA.getCode(), RequestIdSupport.next());
        Command requestCommand = new Command(header, request);
        try {
            Command responseCommand = transport.send(requestCommand).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if (responseHeader.getCode() == ResponseCodeEnum.OK.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
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
