package com.ssen;

import com.ssen.api.SsenServiceApi;
import com.ssen.client.RpcProxy;

/**
 * @Title: RpcClientStarter
 * @Author Jason
 * @Package com.ssen
 * @Date 2024/2/14
 * @description: rpc-client启动器
 */
public class RpcClientStarter {

    public static void main(String[] args) {
        SsenServiceApi o = RpcProxy.create(SsenServiceApi.class);
        System.out.println(o.helloRpc("ssen"));
    }
}
