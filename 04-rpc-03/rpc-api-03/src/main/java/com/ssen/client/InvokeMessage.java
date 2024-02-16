package com.ssen.client;

import lombok.Data;

import java.io.Serializable;

/**
 * @Title: InvokeMessage
 * @Author Jason
 * @Package com.ssen.client
 * @Date 2024/2/14
 * @description: 客户端向服务端传递方法的信息
 */
@Data
public class InvokeMessage implements Serializable {

    private String className;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] args;
}
