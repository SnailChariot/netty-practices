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

    public RpcServerHandler(Map<String, Object> registry) {
        this.registryMap = registry;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("读取到客户端数据：" + msg);



        if (msg instanceof InvokeMessage) {

            InvokeMessage invokeMessage = (InvokeMessage) msg;
            Object result = "服务端并没有这个接口";

            if (registryMap.containsKey(invokeMessage.getClassName())) {
                Object provider = registryMap.get(invokeMessage.getClassName());

                result = provider.getClass()
                        .getMethod(invokeMessage.getMethodName(), invokeMessage.getParamTypes())
                        .invoke(provider, invokeMessage.getArgs());

            }

            // 发送给客户端
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
