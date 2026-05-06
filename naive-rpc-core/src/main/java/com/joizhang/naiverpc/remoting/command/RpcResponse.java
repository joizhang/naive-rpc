package com.joizhang.naiverpc.remoting.command;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

/**
 * RPC响应类
 */
@Getter
@Builder
public class RpcResponse implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 异常信息
     */
    private String error;

    /**
     * 响应体
     */
    private Object body;
}
