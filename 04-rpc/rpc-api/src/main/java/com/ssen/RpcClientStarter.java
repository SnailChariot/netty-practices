package com.ssen;

import com.ssen.api.SsenServiceApi;
import com.ssen.client.RpcProxy;

/**
 * @Title: RpcClientStarter
 * @Author Jason
 * @Package com.ssen
 * @Date 2024/2/14
 * @description: rpc-client客户端（订阅者）启动器
 */
public class RpcClientStarter {

    public static void main(String[] args) {

        SsenServiceApi ssenServiceApi = RpcProxy.create(SsenServiceApi.class);
        System.out.println(ssenServiceApi.hellRpc("用netty手写rpc结束"));
    }
}
