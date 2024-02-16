package com.ssen.api.impl;

import com.ssen.api.SsenServiceApi;

/**
 * @Title: SsenServiceApiImpl
 * @Author Jason
 * @Package com.ssen.api.impl
 * @Date 2024/2/13
 * @description: 接口实现类
 */
public class SsenServiceApiImpl implements SsenServiceApi {

    @Override
    public String hellRpc(String name) {
        return name + "实现类方法";
    }
}
