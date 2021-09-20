package com.joizhang.naiverpc.netty.remoting.transport;

import com.joizhang.naiverpc.netty.remoting.command.CodecTypeEnum;
import com.joizhang.naiverpc.netty.serialize.SerializeSupport;
import com.joizhang.naiverpc.remoting.command.Command;
import com.joizhang.naiverpc.remoting.command.Header;
import com.joizhang.naiverpc.netty.remoting.command.RpcConstants;
import com.joizhang.naiverpc.remoting.command.RpcRequest;
import com.joizhang.naiverpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.util.Arrays;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.SERIALIZER_SERVICE_SUPPORT;

public abstract class CommandDecoder extends LengthFieldBasedFrameDecoder {

    private static final int LENGTH_FIELD_LENGTH = Integer.BYTES;

    public CommandDecoder() {
        /*
         lengthFieldOffset: magic number is 4B
         lengthFieldLength: full length is 4B, so value is 4
         lengthAdjustment: full length include all data and read 16 bytes before,
                           so the left length is (fullLength-16). so values is -16
         initialBytesToStrip: we will check magic code and version manually, so do not strip any bytes. so values is 0
         */
        this(RpcConstants.MAX_FRAME_LENGTH, 12, 4, -16, 0);
    }

    /**
     *
     * @param maxFrameLength Maximum frame length.
     * @param lengthFieldOffset Length field offset.
     * @param lengthFieldLength The number of bytes in the length field.
     * @param lengthAdjustment The compensation value to add to the value of the length field.
     * @param initialBytesToStrip Number of bytes skipped.
     */
    public CommandDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                          int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf byteBuf) throws IOException, ClassNotFoundException {
        if (!byteBuf.isReadable(LENGTH_FIELD_LENGTH)) {
            return null;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt() - LENGTH_FIELD_LENGTH;

        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return null;
        }

        checkMagicNumber(byteBuf);
        Header header = decodeHeader(ctx, byteBuf);
        int payloadLength  = byteBuf.readInt();
        byte [] payload = new byte[payloadLength];
        byteBuf.readBytes(payload);
        return new Command(header, payload);
    }

    private void checkMagicNumber(ByteBuf byteBuf) {
        // read the first 4 bytes which are the magic number
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);
        for (int i = 0; i < len; i++) {
            if (bytes[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(bytes));
            }
        }
    }

    protected abstract Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf);

}
