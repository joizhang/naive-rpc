package com.joizhang.naiverpc.remoting.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CodecTypeEnum {

    NATIVE_JAVA((byte) 0x01, "native java"),
    JAVA((byte) 0x02, "java");

    private final byte code;

    private final String name;

    public static String getName(byte code) {
        for (CodecTypeEnum c : CodecTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

}
