package com.joizhang.naiverpc.remoting.command;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

/**
 * RPC 请求类
 */
@Getter
@Builder
public class RpcRequest implements Serializable {

    /**
     * 接口名
     */
    private final String interfaceName;

    /**
     * 方法名
     */
    private final String methodName;

    /**
     * 参数列表
     */
    private final Object[] args;

    /**
     * 参数列表的Class
     */
    private final Class<?>[] argsTypes;

}
