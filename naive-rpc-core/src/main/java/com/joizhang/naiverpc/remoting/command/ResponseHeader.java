package com.joizhang.naiverpc.remoting.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseHeader extends Header {

    private int code;

    private String error;

    public ResponseHeader(byte rpcVersion, byte messageType, byte codecType, int requestId) {
        this(rpcVersion, messageType, codecType, requestId, ResponseCodeEnum.OK.getCode(), null);
    }

    public ResponseHeader(byte rpcVersion, byte messageType, byte codecType, int requestId, Throwable throwable) {
        this(rpcVersion, messageType, codecType, requestId, ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), throwable.getMessage());
    }

    public ResponseHeader(byte rpcVersion, byte messageType, byte codecType, int requestId, int code, String error) {
        super(rpcVersion, messageType, codecType, requestId);
        this.code = code;
        this.error = error;
    }

}
