package com.joizhang.naiverpc.netty.transport;

import com.joizhang.naiverpc.transport.Transport;
import com.joizhang.naiverpc.transport.InFlightRequests;
import com.joizhang.naiverpc.transport.TransportClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NettyClient implements TransportClient {

    private Bootstrap bootstrap;
    private EventLoopGroup ioEventGroup;
    private final InFlightRequests inFlightRequests;
    private final List<Channel> channels = new LinkedList<>();

    public NettyClient() {
        inFlightRequests = new InFlightRequests();
    }

    @Override
    public Transport createTransport(SocketAddress address, long connectionTimeout)
            throws InterruptedException, TimeoutException {
        return createTransport(address, connectionTimeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public Transport createTransport(SocketAddress address, long connectionTimeout, TimeUnit timeUnit)
            throws InterruptedException, TimeoutException {
        Channel channel = createChannel(address, connectionTimeout, timeUnit);
        return new NettyTransport(channel, inFlightRequests);
    }

    private synchronized Channel createChannel(SocketAddress address, long connectionTimeout, TimeUnit timeUnit)
            throws InterruptedException, TimeoutException {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null!");
        }
        if (ioEventGroup == null) {
            ioEventGroup = newIoEventGroup();
        }
        if (bootstrap == null) {
            bootstrap = newBootstrap();
        }
        ChannelFuture channelFuture = bootstrap.connect(address);
        if (!channelFuture.await(connectionTimeout, timeUnit)) {
            throw new TimeoutException();
        }
        Channel channel = channelFuture.channel();
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException();
        }
        channels.add(channel);
        return channel;
    }

    private EventLoopGroup newIoEventGroup() {
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        } else {
            return new NioEventLoopGroup();
        }
    }

    private Bootstrap newBootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(ioEventGroup)
                .channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(newChannelHandlerPipeline());
        return bootstrap;
    }

    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline()
                        .addLast(new ResponseDecoder())
                        .addLast(new RequestEncoder())
                        .addLast(new ResponseInvocation(inFlightRequests));
            }
        };
    }


    @Override
    public void close() {
        for (Channel channel : channels) {
            if (null != channel) {
                channel.close();
            }
        }
        if (ioEventGroup != null) {
            ioEventGroup.shutdownGracefully();
        }
        inFlightRequests.close();
    }
}
