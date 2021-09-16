package com.joizhang.naiverpc.netty.remoting.server;

import com.joizhang.naiverpc.netty.remoting.transport.CommandDecoder;
import com.joizhang.naiverpc.remoting.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class RequestDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        byte messageType = byteBuf.readByte();
        byte codecType = byteBuf.readByte();
        int version = byteBuf.readInt();
        int requestId = byteBuf.readInt();
        return new Header(messageType, codecType, version, requestId);
    }
}
