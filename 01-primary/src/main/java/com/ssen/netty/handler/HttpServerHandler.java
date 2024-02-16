package com.ssen.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.Charset;

/**
 * @Title: HttpServerHandler
 * @Author Jason
 * @Package com.ssen.netty.handler
 * @Date 2024/2/12
 * @description:
 */
public class HttpServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest clientMsg = (HttpRequest) msg;

            if (clientMsg.getUri().equals("/favicon.ico")) {
                return;
            }
            System.out.println("请求方式：" + clientMsg.getMethod());
            System.out.println("请求uri：" + clientMsg.getUri());

            ByteBuf byteBuf = Unpooled.copiedBuffer("hello netty ssen ！", CharsetUtil.UTF_8);
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());


            ChannelFuture channelFuture = ctx.writeAndFlush(response)
                    .addListener(ChannelFutureListener.CLOSE);


            // 验证异步回调，上面方法异步发送给客户端了，下面异步执行
            if (channelFuture.isSuccess()) {
                Thread.sleep(5000);
                System.out.println("服务端发送数据完毕。。。。");
            }


        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
