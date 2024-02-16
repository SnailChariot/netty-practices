package com.ssen.handler;

import com.ssen.client.InvokeMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Map;

/**
 * @Title: RpcServerHandler
 * @Author Jason
 * @Package com.ssen.handler
 * @Date 2024/2/14
 * @description:
 */
public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    private Map<String, Object> registryMap;


    public RpcServerHandler(Map<String, Object> registryMap) {
        this.registryMap = registryMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务端接收到客户端信息：" + msg);

        if (msg instanceof InvokeMessage) {
            InvokeMessage invokeMesssge = (InvokeMessage) msg;

            Object result = "服务端并没有该接口";

            if (registryMap.containsKey(invokeMesssge.getClassName())) {
                Object providerImpl = registryMap.get(invokeMesssge.getClassName());
                result = providerImpl.getClass()
                        .getMethod(invokeMesssge.getMethodName(), invokeMesssge.getParamTypes())
                        .invoke(providerImpl, invokeMesssge.getArgs());
            }

            // 发送给客户端数据
            ctx.channel().writeAndFlush(result).sync();
            ctx.close().sync();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
