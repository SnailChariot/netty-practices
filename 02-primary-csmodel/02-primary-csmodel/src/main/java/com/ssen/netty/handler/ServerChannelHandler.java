package com.ssen.netty.handler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Title: ServerChannelHandler
 * @Author Jason
 * @Package com.ssen.netty.handler
 * @Date 2024/2/12
 * @description:
 */
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("来自client：" + ctx.channel().remoteAddress() + ", msg：" + msg);

        ctx.channel().writeAndFlush("服务端发送：" + UUID.randomUUID());

        TimeUnit.MILLISECONDS.sleep(500);

        ctx.channel().writeAndFlush("添加监听器")
                .addListener(ChannelFutureListener.CLOSE_ON_FAILURE); // 只有当异常的时候断开连接
        ctx.channel().writeAndFlush("添加监听器：只有当异常的时候断开连接")
                .addListener(ChannelFutureListener.CLOSE); // 发送完这条数据就断开
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
