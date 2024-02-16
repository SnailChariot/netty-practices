package com.ssen;

import com.ssen.server.RpcServer;

/**
 * @Title: RpcServerStarter
 * @Author Jason
 * @Package com.ssen
 * @Date 2024/2/14
 * @description: rpc-server启动器
 */
public class RpcServerStarter {

    public static void main(String[] args) {

        new RpcServer().publish("com.ssen.api.impl");
    }
}
