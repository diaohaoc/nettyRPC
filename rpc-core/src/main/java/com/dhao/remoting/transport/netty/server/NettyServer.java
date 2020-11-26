package com.dhao.remoting.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create by DiaoHao on 2020/11/26 22:14
 */
public class NettyServer {

    private Logger log = LoggerFactory.getLogger(NettyServer.class);

    private static int PORT = 7000;

    //服务器的启动
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(null); //解码器
                            pipeline.addLast(null); //编码器
                            pipeline.addLast(null); //业务处理器
                        }
                    });
            ChannelFuture future = bootstrap.bind(PORT).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("NettyServer startup error:", e);
        }finally {
            log.error("bossGroup and workerGroup shutdownGracefully");
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
