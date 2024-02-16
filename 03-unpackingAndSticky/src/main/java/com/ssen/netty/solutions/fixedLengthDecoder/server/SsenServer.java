package com.ssen.netty.solutions.fixedLengthDecoder.server;

import com.ssen.netty.solutions.fixedLengthDecoder.handler.ServerChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;


/**
 * @Title: SsenServer
 * @Author Jason
 * @Package com.ssen.netty.server
 * @Date 2024/2/12
 * @description: netty-server
 */
public class SsenServer {


    public static void main(String[] args) {
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 此时的长度是客户端发送数据的长度，已约定好的
                            pipeline.addLast(new FixedLengthFrameDecoder(10));
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));

                            pipeline.addLast(new ServerChannelHandler());

                        }
                    });

            ChannelFuture future = serverBootstrap.bind(8888).sync();
            System.out.println("服务端启动完毕。。。。");

            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
