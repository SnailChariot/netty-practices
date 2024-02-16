package com.ssen.handler;

import com.ssen.client.InvokeMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.HashMap;
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

        System.out.println("rpc-server收到客户端消息：" + msg);

        if (msg instanceof InvokeMessage) {
            InvokeMessage method = (InvokeMessage) msg;
            Object result = "rpc-server端没有该方法";

            // 判断是否存在该方法
            if (registryMap.containsKey(method.getClassName())) {
                Object provider = registryMap.get(method.getClassName());
                result = provider.getClass()
                        .getMethod(method.getMethodName(), method.getParamTypes())
                        .invoke(provider, method.getParamValues());
            }
            // 把方法返回结果，发给订阅者
            ctx.channel().writeAndFlush(result);
            ctx.close();
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
