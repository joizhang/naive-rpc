package com.joizhang.naiverpc.remoting.command;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class RpcResponse implements Serializable {

    private int code;

    private String error;

    private Object body;

}
