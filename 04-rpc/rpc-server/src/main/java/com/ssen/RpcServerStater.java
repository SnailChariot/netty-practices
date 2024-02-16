package com.ssen;

import com.ssen.server.RpcServer;

/**
 * @Title: RpcServerStater
 * @Author Jason
 * @Package com.ssen
 * @Date 2024/2/14
 * @description: 服务端启动器
 */
public class RpcServerStater {

    public static void main(String[] args) throws Exception {
        new RpcServer().publish("com.ssen.api.impl");
    }
}
