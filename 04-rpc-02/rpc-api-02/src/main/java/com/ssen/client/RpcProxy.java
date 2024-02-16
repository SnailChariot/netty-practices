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
 * @description: rpc-client代理类
 */
public class RpcProxy {

    public static <T> T create(Class<?> clazz) {
        
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (Object.class.equals(clazz.getDeclaringClass())) {
                            return invoke(proxy, method, args);
                        }
                        
                        return rpcInvoker(clazz, method, args);
                    }
                });
    }

    private static Object rpcInvoker(Class<?> clazz, Method method, Object[] args) {

        NioEventLoopGroup eventGroup = new NioEventLoopGroup();
        RpcClientHandler rpcClientHandler = new RpcClientHandler();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventGroup)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .channel(NioSocketChannel.class)
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

            // 指定连接服务器的地址
            ChannelFuture future = bootstrap.connect("localhost", 9999).sync();
            System.out.println("连接服务端成功。。。");

            InvokeMessage invokeMessage = new InvokeMessage();
            invokeMessage.setClassName(clazz.getName());
            invokeMessage.setParamTypes(method.getParameterTypes());
            invokeMessage.setMethodName(method.getName());
            invokeMessage.setArgs(args);

            // 发送服务端
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
