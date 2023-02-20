package com.joizhang.naiverpc.netty.remoting.client;

import com.joizhang.naiverpc.remoting.command.Command;
import com.joizhang.naiverpc.remoting.transport.InFlightRequests;
import com.joizhang.naiverpc.remoting.transport.ResponseFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Slf4j
@ChannelHandler.Sharable
public class ResponseInvocation extends SimpleChannelInboundHandler<Command> {

    private final InFlightRequests inFlightRequests;

    ResponseInvocation(InFlightRequests inFlightRequests) {
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, @NotNull Command response) {
        ResponseFuture future = inFlightRequests.remove(response.getHeader().getRequestId());
        if (!Objects.isNull(future)) {
            future.getFuture().complete(response);
        } else {
            log.warn("Drop response: {}", response);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Exception: ", cause);
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }

}