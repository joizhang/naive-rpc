package com.joizhang.naiverpc.netty.remoting.transport;

import com.joizhang.naiverpc.remoting.command.Header;
import com.joizhang.naiverpc.netty.remoting.command.RpcConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public abstract class CommandDecoder extends LengthFieldBasedFrameDecoder {

    private static final int LENGTH_FIELD_LENGTH = Integer.BYTES;

    public CommandDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, 5, 4, -9, 0);
    }

    public CommandDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                          int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf byteBuf) {
        if (!byteBuf.isReadable(LENGTH_FIELD_LENGTH)) {
            return null;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt() - LENGTH_FIELD_LENGTH;

        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return null;
        }

        Header header = decodeHeader(ctx, byteBuf);
//        int payloadLength  = length - header.length();
//        byte [] payload = new byte[payloadLength];
//        byteBuf.readBytes(payload);
//        list.add(new Command(header, payload));
        return null;
    }

    protected abstract Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf);

}
