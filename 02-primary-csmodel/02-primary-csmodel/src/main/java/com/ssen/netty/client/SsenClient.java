package com.ssen.netty.client;

import com.ssen.netty.handler.ClientChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @Title: SsenClient
 * @Author Jason
 * @Package com.ssen.netty.client
 * @Date 2024/2/12
 * @description: netty-client
 */
public class SsenClient {

    public static void main(String[] args) {


        NioEventLoopGroup eventGroup = new NioEventLoopGroup();

        try {

            Bootstrap bootstrap = new Bootstrap();


            bootstrap.group(eventGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new ClientChannelHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect("localhost", 8888).sync();
            System.out.println("客户端连接服务端成功。。。。。。");
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (eventGroup != null) {
                eventGroup.shutdownGracefully();
            }
        }
    }
}
