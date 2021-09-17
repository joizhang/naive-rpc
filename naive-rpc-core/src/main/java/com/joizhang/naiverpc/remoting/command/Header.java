package com.joizhang.naiverpc.remoting.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
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
