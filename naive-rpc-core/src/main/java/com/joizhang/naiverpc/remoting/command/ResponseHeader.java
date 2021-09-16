package com.joizhang.naiverpc.remoting.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseHeader extends Header {

    private int code;

    private String error;

    public ResponseHeader(byte messageType, byte codecType, int version, int requestId) {
        this(messageType, codecType, version, requestId, ResponseCodeEnum.OK.getCode(), null);
    }

    public ResponseHeader(byte messageType, byte codecType, int version, int requestId, Throwable throwable) {
        this(messageType, codecType, version, requestId, ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), throwable.getMessage());
    }

    public ResponseHeader(byte messageType, byte codecType, int version, int requestId, int code, String error) {
        super(messageType, codecType, version, requestId);
        this.code = code;
        this.error = error;
    }

}
