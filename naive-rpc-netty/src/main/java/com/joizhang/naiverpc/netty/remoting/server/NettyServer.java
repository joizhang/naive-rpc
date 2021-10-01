package com.joizhang.naiverpc.netty.remoting.server;

import com.joizhang.naiverpc.netty.remoting.NettyEventLoopFactory;
import com.joizhang.naiverpc.remoting.server.TransportServer;
import com.joizhang.naiverpc.remoting.transport.RequestHandlerRegistry;
import com.joizhang.naiverpc.utils.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateHandler;

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
        this.bossGroup = NettyEventLoopFactory.eventLoopGroup(1, "NettyServerBoss");
        this.workerGroup = NettyEventLoopFactory.eventLoopGroup(Constants.DEFAULT_IO_THREADS, "NettyServerWorker");
        this.bootstrap = newBootstrap();
        this.channel = doBind();
    }


    private ServerBootstrap newBootstrap() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(this.bossGroup, this.workerGroup)
                .channel(NettyEventLoopFactory.serverSocketChannelClass())
                .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(newChannelHandlerPipeline());
        return serverBootstrap;
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
