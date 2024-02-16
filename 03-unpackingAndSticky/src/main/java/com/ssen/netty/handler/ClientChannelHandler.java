package com.ssen.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
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
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] bytes = "hello ssen".getBytes();
        ByteBuf buffer = null;
        for (int i = 0; i < 2; i++) {
            buffer = Unpooled.buffer(bytes.length);
            buffer.writeBytes(bytes);
            ctx.channel().writeAndFlush(buffer);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
