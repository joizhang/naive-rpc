package com.joizhang.naiverpc.netty.remoting.server;

import com.joizhang.naiverpc.netty.remoting.transport.CommandEncoder;
import com.joizhang.naiverpc.remoting.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ResponseEncoder extends CommandEncoder {

    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf)
            throws Exception {
        super.encodeHeader(channelHandlerContext, header, byteBuf);
    }

}