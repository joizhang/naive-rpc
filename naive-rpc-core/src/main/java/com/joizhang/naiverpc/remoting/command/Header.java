package com.joizhang.naiverpc.remoting.command;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Header {

    /**
     * 版本
     */
    private byte rpcVersion;

    /**
     * 类型
     */
    private byte messageType;

    /**
     * 序列化
     */
    private byte codecType;

    /**
     * 标识唯一请求
     */
    private int requestId;

}
