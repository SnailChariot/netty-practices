package com.ssen.netty.server;

import com.ssen.netty.handler.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Title: SsenServer
 * @Author Jason
 * @Package com.ssen.netty.server
 * @Date 2024/2/12
 * @description: 服务端
 */
public class SsenServer {

    public static void main(String[] args) {

        NioEventLoopGroup parentEventLoop = new NioEventLoopGroup();
        NioEventLoopGroup childEventLoop = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(parentEventLoop, childEventLoop)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpServerHandler());
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(8888).sync();
            System.out.println("服务端上线。。。。。。。");
            future.channel().closeFuture().sync();
            System.out.println("服务端通道关闭。。。。。。。");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentEventLoop.shutdownGracefully();
            childEventLoop.shutdownGracefully();
        }

    }
}
