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
import java.lang.reflect.AnnotatedType;
import java.net.URL;
import java.util.*;

/**
 * @Title: RpcServer
 * @Author Jason
 * @Package com.ssen.server
 * @Date 2024/2/13
 * @description: rpc-server
 */
public class RpcServer {


    private Map<String, Object> registryMap = new HashMap<>();
    private List<String> classCache = new ArrayList<>();

    // 1.发布
    // 1.1查找指定目录下的所有接口，放入一个集合中
    // 1.2遍历所有接口，放入一个map中存放接口路径及接口名称
    // 1.3发送方法相关信息


    public void publish(String providerPackage) throws Exception {

        getProviderClass(providerPackage);
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
                            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            pipeline.addLast(new RpcServerHandler(registryMap));
                        }
                    });

            ChannelFuture future = serverBootstrap.bind(9999).sync();
            System.out.println("服务端监听9999端口，启动成功。。。");
            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }

    private void doRegister() throws Exception {

        if (classCache.size() > 0) {
            for (String className :classCache){

                Class<?> clazz = Class.forName(className);
                String interfaceName = clazz.getInterfaces()[0].getName();
                registryMap.put(interfaceName, clazz.newInstance());
            }
        }


    }


    /**
     * 获取当前目录下的所有接口，汇总到一个集合中
     * @param providerPackage
     */
    private void getProviderClass(String providerPackage) {

        URL resource = this.getClass().getClassLoader().getResource(providerPackage.replaceAll("\\.", "/"));

        File file = null;
        if (resource != null) {
            file = new File(resource.getFile());
        }

        if (file != null) {
            for (File f : Objects.requireNonNull(file.listFiles())) {
                if (f.isDirectory()) {
                    getProviderClass(providerPackage + "." + f.getName());
                } else if (f.getName().endsWith(".class")) {
                    String fileName = f.getName().replace(".class", "").trim();
                    classCache.add(providerPackage + "." + fileName);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            File file = new File("com/ssen/handler/RpcServerHandler");
            String name = file.getName();
            Class<?> aClass = Class.forName("com.ssen.handler.RpcServerHandler");
            String name1 = aClass.getName();

            Class<?>[] interfaces = aClass.getInterfaces();
            AnnotatedType[] annotatedInterfaces = aClass.getAnnotatedInterfaces();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
