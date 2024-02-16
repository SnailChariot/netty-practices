package com.ssen.netty.handler;

import io.netty.channel.*;
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
public class ServerChannelHandler extends SimpleChannelInboundHandler<String> {

    private int num;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("收到客户端第：{" + ++num + "}个数据包：" + s);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
