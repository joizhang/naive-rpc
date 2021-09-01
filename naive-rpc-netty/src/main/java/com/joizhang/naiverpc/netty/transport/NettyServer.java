package com.joizhang.naiverpc.netty.transport;

import com.joizhang.naiverpc.transport.RequestHandlerRegistry;
import com.joizhang.naiverpc.transport.TransportServer;
import com.joizhang.naiverpc.utils.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class NettyServer implements TransportServer {

    private int port;
    private RequestHandlerRegistry requestHandlerRegistry;
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;

    @Override
    public void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception {
        this.port = port;
        this.requestHandlerRegistry = requestHandlerRegistry;
        this.bossGroup = newEventLoopGroup(1, "NaiveRPCServerBoss");
        this.workerGroup = newEventLoopGroup(Constants.DEFAULT_IO_THREADS, "NaiveRPCServerWorker");
        this.bootstrap = newBootstrap();
        this.channel = doBind();
    }

    private EventLoopGroup newEventLoopGroup(int nThreads, String threadFactoryName) {
        ThreadFactory threadFactory = new DefaultThreadFactory(threadFactoryName, true);
        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup(nThreads, threadFactory);
        } else {
            return new NioEventLoopGroup(nThreads, threadFactory);
        }
    }

    private ServerBootstrap newBootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(this.bossGroup, this.workerGroup)
                .channel(serverSocketChannelClass())
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(newChannelHandlerPipeline());
        return serverBootstrap;
    }

    private Class<? extends ServerChannel> serverSocketChannelClass() {
        return Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline()
                        .addLast("decoder", new RequestDecoder())
                        .addLast("encoder", new ResponseEncoder())
                        .addLast("server-idle-handler", new IdleStateHandler(0, 20, 0, TimeUnit.SECONDS))
                        .addLast("handler", new RequestInvocation(requestHandlerRegistry));
            }
        };
    }

    private Channel doBind() {
        ChannelFuture channelFuture = bootstrap.bind(port);
        channelFuture.syncUninterruptibly();
        return channelFuture.channel();
    }

    @Override
    public void stop() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (channel != null) {
            channel.close();
        }
    }

}
