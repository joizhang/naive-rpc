package com.joizhang.naiverpc.netty.remoting.transport;

import com.joizhang.naiverpc.netty.remoting.command.CodecTypeEnum;
import com.joizhang.naiverpc.netty.serialize.SerializeSupport;
import com.joizhang.naiverpc.remoting.command.Command;
import com.joizhang.naiverpc.remoting.command.Header;
import com.joizhang.naiverpc.netty.remoting.command.RpcConstants;
import com.joizhang.naiverpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static com.joizhang.naiverpc.spi.ServiceSupportConstant.SERIALIZER_SERVICE_SUPPORT;

/**
 * <pre>
 * +--------------------+--------------------+--------------------+--------------------+
 * |         0          |         1          |        2           |        3           |
 * +--------------------+--------------------+--------------------+--------------------+
 * |                                   4B magic number                                 |
 * +--------------------+--------------------+--------------------+--------------------+
 * |                                   4B full length                                  |
 * +--------------------+--------------------+--------------------+--------------------+
 * | 1B rpc version     | 1B message type    | 1B codec type      | 1B padding         |
 * +--------------------+--------------------+--------------------+--------------------+
 * |                                   4B requestId                                    |
 * +--------------------+--------------------+--------------------+--------------------+
 * |                                      payload                                      |
 * +--------------------+--------------------+--------------------+--------------------+
 * </pre>
 */
public class CommandEncoder extends MessageToByteEncoder<Command> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Command msg, ByteBuf byteBuf)
            throws Exception {
        int fullLength = RpcConstants.HEAD_LENGTH;
        byteBuf.writeBytes(RpcConstants.MAGIC_NUMBER);
        // leave a place to write the value of full length
        byteBuf.writerIndex(byteBuf.writerIndex() + 4);
        // write header
        encodeHeader(channelHandlerContext, msg.getHeader(), byteBuf);

        // write object
        String codecName = CodecTypeEnum.getName(msg.getHeader().getCodecType());
        Serializer serializer = SERIALIZER_SERVICE_SUPPORT.getService(codecName);
        byte[] bytes = SerializeSupport.serialize(serializer, msg.getPayload());
        fullLength += bytes.length;
        byteBuf.writeBytes(bytes);

        // write full length
        int writeIndex = byteBuf.writerIndex();
        byteBuf.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length);
        byteBuf.writeInt(fullLength);
        byteBuf.writerIndex(writeIndex);
    }

    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf)
            throws Exception {
        byteBuf.writeByte(header.getRpcVersion());
        byteBuf.writeByte(header.getMessageType());
        byteBuf.writeByte(header.getCodecType());
        byteBuf.writeByte(RpcConstants.PADDING);
        byteBuf.writeInt(header.getRequestId());
    }

}
