package com.ssen.client;

import lombok.Data;

import java.io.Serializable;

/**
 * @Title: InvokeMessage
 * @Author Jason
 * @Package com.ssen.client
 * @Date 2024/2/14
 * @description: 订阅者向发布者传递的方法信息
 */
@Data
public class InvokeMessage implements Serializable {

    private String methodName;

    private String className;

    /**
     * 方法参数类型
     */
    private Class<?>[] paramTypes;
    /**
     * 方法参数值
     */
    private Object[] paramValues;
}
