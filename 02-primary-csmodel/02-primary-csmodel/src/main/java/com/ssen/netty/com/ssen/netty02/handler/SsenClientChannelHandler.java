package com.ssen.netty.com.ssen.netty02.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.time.LocalDateTime;

/**
 * @Title: SsenClientChannelHandler
 * @Author Jason
 * @Package com.ssen.netty.com.ssen.netty02.handler
 * @Date 2024/2/13
 * @description:
 */
public class SsenClientChannelHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("接收到服务端数据：" + s);

        channelHandlerContext.channel().writeAndFlush("客户端发送数据：" + LocalDateTime.now());


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("我是客户端：" + ctx.channel().remoteAddress());
    }
}
