package com.ssen.api.impl;

import com.ssen.api.SsenServiceApi;

/**
 * @Title: SsenServiceApiImpl
 * @Author Jason
 * @Package com.ssen.api.impl
 * @Date 2024/2/14
 * @description: 实现类
 */
public class SsenServiceApiImpl implements SsenServiceApi {
    @Override
    public String helloRpc(String name) {
        return "你好，" + name + "，你又通过rpc调用了我一下";
    }
}
