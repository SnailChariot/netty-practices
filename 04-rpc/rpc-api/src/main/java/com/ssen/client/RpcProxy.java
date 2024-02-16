package com.ssen.client;

import com.ssen.handler.RpcClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Title: RpcProxy
 * @Author Jason
 * @Package com.ssen.client
 * @Date 2024/2/14
 * @description: 订阅者（客户端）代理类
 */
public class RpcProxy {


    public static <T> T create(Class<?> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (Object.class.equals(method.getDeclaringClass())) {
                            return method.invoke(proxy, method, args);
                        }

                        // 通过netty将接口信息发送给提供者，获取指定方法
                        return RpcInvoke(clazz, method, args);
                    }
                });
    }

    private static Object RpcInvoke(Class<?> clazz, Method method, Object[] args) {
        NioEventLoopGroup eventGroup = new NioEventLoopGroup();

        RpcClientHandler rpcClientHandler = new RpcClientHandler();

        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(rpcClientHandler);
                        }
                    });

            // 绑定指定服务地址
            ChannelFuture future = bootstrap.connect("localhost", 9999).sync();

            // 指定接口信息发送给提供者
            InvokeMessage invokeMessage = new InvokeMessage();
            invokeMessage.setClassName(clazz.getName());
            invokeMessage.setMethodName(method.getName());
            invokeMessage.setParamTypes(method.getParameterTypes());
            invokeMessage.setParamValues(args);
            future.channel().writeAndFlush(invokeMessage).sync();

            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventGroup.shutdownGracefully();
        }
        return rpcClientHandler.getResult();
    }

}
