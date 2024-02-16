package com.ssen.server;

import com.ssen.handler.RpcServerHandler;
import com.sun.xml.internal.bind.api.ClassResolver;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * @Title: RpcServer
 * @Author Jason
 * @Package com.ssen.server
 * @Date 2024/2/14
 * @description: rpc-server，接收客户的方法信息，并返回方法返回值
 */
public class RpcServer {


    private List<String> classCacheList = new ArrayList<>();
    private Map<String, Object> registry = new HashMap<>();

    public void publish(String providerPackage) throws InterruptedException {
        // 遍历指定目录，添加类集合
        getProviderClassList(providerPackage);
        // 注册：对类集合进行遍历，放入到一个类名，全限定名的map中
        doRegister();

        // netty实现服务端通信
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup, childGroup)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new RpcServerHandler(registry));
                        }
                    });

            // 监听固定端口
            ChannelFuture future = serverBootstrap.bind(9999).sync();

            System.out.println("服务端开启9999，启动完毕。。。");
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }



    }

    private void doRegister() {

        if (classCacheList.size() == 0) {
            return;
        }

        for (String className : classCacheList) {

            try {
                Class<?> aClass = Class.forName(className);
                String infName = aClass.getInterfaces()[0].getName();
                registry.put(infName, aClass.newInstance());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }

    }


    private void getProviderClassList(String providerPackage) {
        URL resource = this.getClass().getClassLoader()
                .getResource(providerPackage.replaceAll("\\.", "/"));

        if (resource == null) {
            return;
        }

        File file = new File(resource.getFile());
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    getProviderClassList(providerPackage + "." + f.getName());
                } else if (f.getName().endsWith(".class")) {
                    String className = f.getName().replace(".class", "");
                    classCacheList.add(providerPackage + "." + className);
                }
            }
        }


    }
}
