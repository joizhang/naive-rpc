package com.joizhang.naiverpc.remoting.command;

import java.util.HashMap;
import java.util.Map;

public enum ResponseCodeEnum {

    CONTINUE(100, "Continue"),
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable");

    private static final Map<Integer, ResponseCodeEnum> codes = new HashMap<>();
    private final int code;
    private final String message;

    static {
        for (ResponseCodeEnum responseCode : ResponseCodeEnum.values()) {
            codes.put(responseCode.code, responseCode);
        }
    }

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseCodeEnum valueOf(int code) {
        return codes.get(code);
    }

    public int getCode() {
        return code;
    }

    public String getMessage(Object... args) {
        if (args.length < 1) {
            return message;
        }
        return String.format(message, args);
    }

}