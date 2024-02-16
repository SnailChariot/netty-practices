package com.ssen.client;

import lombok.Data;

import java.io.Serializable;

/**
 * @Title: InvokeMessage
 * @Author Jason
 * @Package com.ssen.client
 * @Date 2024/2/14
 * @description: 客户端向提供者发送方法相关信息类
 */
@Data
public class InvokeMessage implements Serializable {

    private String className;

    private String methodName;

    private Class<?>[] paramTypes;

    private Object[] args;

}
