package com.ssen.server;

import com.ssen.handler.RpcServerHandler;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: RpcServer
 * @Author Jason
 * @Package com.ssen.server
 * @Date 2024/2/14
 * @description: rpc-server
 */
public class RpcServer {

    private List<String> classCacheList = new ArrayList<>();

    private Map<String, Object> registry = new HashMap<>();


    // 发布
    public void publish(String providerPackage) {
        // 获取指定包下实现类
        getProviderClassesList(providerPackage);
        // 注册到map中
        doRegister();

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

            // 监听端口号
            ChannelFuture future = serverBootstrap.bind(9999).sync();
            System.out.println("服务端9999，上线完毕。。。");
            System.out.println("registry：" + registry);

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
                Class<?> clazz = Class.forName(className);
                String interfaceName = clazz.getInterfaces()[0].getName();
                registry.put(interfaceName, clazz.newInstance());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void getProviderClassesList(String providerPackage) {

        URL resource = this.getClass().getClassLoader().getResource(providerPackage.replaceAll("\\.", "/"));

        if (resource == null) {
            return;
        }

        File file = new File(resource.getFile());
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {

                if (f.isDirectory()) {
                    getProviderClassesList(providerPackage);
                } else if (f.getName().endsWith(".class")) {
                    classCacheList.add(providerPackage + "." + f.getName()
                            .replace(".class", ""));
                }
            }
        }
    }


}
