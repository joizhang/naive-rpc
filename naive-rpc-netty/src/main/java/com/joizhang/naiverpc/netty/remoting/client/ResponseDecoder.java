package com.joizhang.naiverpc.netty.remoting.client;

import com.joizhang.naiverpc.netty.remoting.transport.CommandDecoder;
import com.joizhang.naiverpc.remoting.command.Header;
import com.joizhang.naiverpc.remoting.command.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class ResponseDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        byte messageType = byteBuf.readByte();
        byte codecType = byteBuf.readByte();
        byte version = byteBuf.readByte();
        int requestId = byteBuf.readInt();
        int code = byteBuf.readInt();
        int errorLength = byteBuf.readInt();
        byte[] errorBytes = new byte[errorLength];
        byteBuf.readBytes(errorBytes);
        String error = new String(errorBytes, StandardCharsets.UTF_8);
        return new ResponseHeader(version, messageType, codecType, requestId, code, error);
    }

}