package com.ssen.netty.solutions.delimiterDecoder.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Title: ClientChannelHandler
 * @Author Jason
 * @Package com.ssen.netty.handler
 * @Date 2024/2/12
 * @description:
 */
public class ClientChannelHandler extends ChannelInboundHandlerAdapter {

    private String msg = "This document contains release notes for the changes in MySQL 8.3. For information about changes in a different version of MySQL, see the release notes for||----|| that version.For additional MySQL 8.3 documentation, see the MySQL 8.3 Reference Manual, which includes an overview of features added in MySQL 8.3 (What Is New in MySQL 8.3), and discussion of upgrade issues that you may encounter for upgrades from MySQL 8.2 to MySQL 8.3 (Changes in MySQL 8.3).Before||----|| upgrading to MySQL 8.3, review the information in https://dev.mysql.com/doc/refman/8.3/en/upgrading.html and perform||----|| any recommended actions. Perform the upgrade on a test system first to make sure everything works smoothly, and then on the production system.Downgrade from MySQL 8.3 to MySQL 8.2, or from a MySQL 8.3 release to a previous MySQL 8.3 release, is not supported. The only supported alternative is to restore a backup taken before upgrading. It is therefore imperative that you back up your data before starting the upgrade process.MySQL platform support evolves over time; please refer to https://www.mysql.com/support/supportedplatforms/database.html for the||----|| latest updates.Updates to these notes occur as new product features are added, so that everybody can follow the development process. If a recent version is listed here that you cannot find on the download page (https://dev.mysql.com/downloads/), the version has not yet been released.The documentation included in source and binary distributions may not be fully up to date with respect to release note entries because integration of the documentation occurs at release build time. For the most up-to-date release notes, please refer to the online documentation instead.00000000000000000000000000000000000000000000000000"
            + System.getProperty("line.separator");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] bytes = msg.getBytes();
        ByteBuf buffer = null;
        for (int i = 0; i < 2; i++) {
            buffer = Unpooled.buffer(bytes.length);
            buffer.writeBytes(bytes);
            ctx.channel().writeAndFlush(buffer);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
