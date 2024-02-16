package com.ssen.api.impl;

import com.ssen.api.SsenServiceApi;

/**
 * @Title: SsenServiceApiImpl
 * @Author Jason
 * @Package com.ssen.api.impl
 * @Date 2024/2/14
 * @description: 接口实现类
 */
public class SsenServiceApiImpl implements SsenServiceApi {
    @Override
    public String helloRpc(String name) {
        return "你好，" + name + "，这是第四次手写rpc";
    }
}
