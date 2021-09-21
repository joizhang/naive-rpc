package com.joizhang.naiverpc.netty.remoting.transport;

import com.joizhang.naiverpc.netty.remoting.command.CodecTypeEnum;
import com.joizhang.naiverpc.netty.remoting.command.MessageType;
import com.joizhang.naiverpc.netty.remoting.command.RpcConstants;
import com.joizhang.naiverpc.netty.serialize.SerializeSupport;
import com.joizhang.naiverpc.remoting.command.Command;
import com.joizhang.naiverpc.remoting.command.Header;
import com.joizhang.naiverpc.remoting.command.RpcRequest;
import com.joizhang.naiverpc.remoting.command.RpcResponse;
import com.joizhang.naiverpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.SERIALIZER_SERVICE_SUPPORT;

public class CommandDecoder extends LengthFieldBasedFrameDecoder {

    public CommandDecoder() {
        /*
         lengthFieldOffset: magic number is 4B, so value is 4
         lengthFieldLength: full length is 4B, so value is 4
         lengthAdjustment: full length include all data and read 8 bytes before,
                           so the left length is (fullLength-8). so values is -8
         initialBytesToStrip: we will check magic code and version manually, so do not strip any bytes. so values is 0
         */
        this(RpcConstants.MAX_FRAME_LENGTH, 4, 4, -8, 0);
    }

    /**
     * @param maxFrameLength      Maximum frame length.
     * @param lengthFieldOffset   Length field offset.
     * @param lengthFieldLength   The number of bytes in the length field.
     * @param lengthAdjustment    The compensation value to add to the value of the length field.
     * @param initialBytesToStrip Number of bytes skipped.
     */
    public CommandDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                          int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        Object decoded = super.decode(ctx, byteBuf);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) decoded;
            if (frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
                checkMagicNumber(frame);
                int fullLength = frame.readInt();
                Header header = decodeHeader(ctx, frame);
                // deserialize the object
                int payloadLength = fullLength - RpcConstants.HEAD_LENGTH;
                byte[] payload = new byte[payloadLength];
                frame.readBytes(payload);
                String codecName = CodecTypeEnum.getName(header.getCodecType());
                Serializer serializer = SERIALIZER_SERVICE_SUPPORT.getService(codecName);
                return getCommand(header, payload, serializer);
            }
        }
        return decoded;
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

    private Command getCommand(Header header, byte[] payload, Serializer serializer)
            throws IOException, ClassNotFoundException {
        Command command;
        switch (header.getMessageType()) {
            case MessageType.REQUEST_TYPE:
                RpcRequest rpcRequest = SerializeSupport.deserialize(serializer, payload, RpcRequest.class);
                command = new Command(header, rpcRequest);
                break;
            case MessageType.RESPONSE_TYPE:
                RpcResponse rpcResponse = SerializeSupport.deserialize(serializer, payload, RpcResponse.class);
                command = new Command(header, rpcResponse);
                break;
            default:
                command = new Command(header, new byte[0]);
                break;
        }
        return command;
    }

    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        byte rpcVersion = byteBuf.readByte();
        byte messageType = byteBuf.readByte();
        byte codecType = byteBuf.readByte();
        byte padding = byteBuf.readByte();
        int requestId = byteBuf.readInt();
        return Header.builder()
                .rpcVersion(rpcVersion)
                .messageType(messageType)
                .codecType(codecType)
                .requestId(requestId).build();
    }

}
