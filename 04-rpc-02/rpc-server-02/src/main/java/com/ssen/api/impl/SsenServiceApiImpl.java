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
    public String hellRpc(String name) {
        return "hello Mr." + name + "，恭喜你练习了第三次手写rpc";
    }
}
