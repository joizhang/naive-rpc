package com.joizhang.naiverpc.remoting.command;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

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
