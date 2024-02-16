package com.ssen.netty.com.ssen.netty02.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.UUID;

/**
 * @Title: SsenServerChannelHandler
 * @Author Jason
 * @Package com.ssen.netty.com.ssen.netty02.handler
 * @Date 2024/2/13
 * @description:
 */
public class SsenServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("接收到客户端信息：" + msg);
        ctx.channel().writeAndFlush("服务端发送信息：" + UUID.randomUUID());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
