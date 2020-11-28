package com.dhao.remoting.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Create by DiaoHao on 2020/11/26 22:14
 */
public class NettyServer {

    private final Logger log = LoggerFactory.getLogger(NettyServer.class);

    private final static int PORT = 7000;

    //服务器的启动
    public void start() throws UnknownHostException {
        String host = InetAddress.getLocalHost().getHostName();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //心跳检测，30秒没有读事件，则发送心跳包检测是否处于连接状态
                            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                            //pipeline.addLast(null); //解码器
                            //pipeline.addLast(null); //编码器
                            //pipeline.addLast(null); //业务处理器
                        }
                    });
            ChannelFuture future = bootstrap.bind(host, PORT).sync();
            log.info("NettyServer start....");
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
