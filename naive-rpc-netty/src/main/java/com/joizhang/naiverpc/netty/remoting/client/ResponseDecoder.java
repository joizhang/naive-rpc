package com.joizhang.naiverpc.netty.remoting.client;

import com.joizhang.naiverpc.netty.remoting.transport.CommandDecoder;
import com.joizhang.naiverpc.remoting.command.Header;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端响应编码器
 */
public class ResponseDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        return super.decodeHeader(channelHandlerContext, byteBuf);
    }

}