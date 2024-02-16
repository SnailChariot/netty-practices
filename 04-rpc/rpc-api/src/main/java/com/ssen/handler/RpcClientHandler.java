package com.ssen.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @Title: RpcClientHandler
 * @Author Jason
 * @Package com.ssen.handler
 * @Date 2024/2/14
 * @description:
 */
public class RpcClientHandler extends SimpleChannelInboundHandler {

    private Object result;

    public Object getResult() {
        return this.result;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("接收到服务端信息：" + o);
        this.result = o;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
