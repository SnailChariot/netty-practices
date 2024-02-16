package com.ssen.netty.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @Title: ClientChannelHandler
 * @Author Jason
 * @Package com.ssen.netty.handler
 * @Date 2024/2/12
 * @description:
 */
public class ClientChannelHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("客户端首先发话。。。。");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        System.out.println("收到服务端数据：" + s);
        channelHandlerContext.channel().writeAndFlush("客户端发送数据：" + LocalDateTime.now());
        TimeUnit.MILLISECONDS.sleep(500);
    }
}
