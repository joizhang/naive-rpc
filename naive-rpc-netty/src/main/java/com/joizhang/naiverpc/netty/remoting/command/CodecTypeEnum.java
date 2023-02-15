package com.joizhang.naiverpc.netty.remoting.command;

import com.joizhang.naiverpc.netty.serialize.java.JavaSerializer;
import com.joizhang.naiverpc.netty.serialize.metadata.MetadataSerializer;
import com.joizhang.naiverpc.netty.serialize.nativejava.NativeJavaSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CodecTypeEnum {

    NATIVE_JAVA((byte) 0x01, NativeJavaSerializer.class.getCanonicalName()),
    JAVA((byte) 0x02, JavaSerializer.class.getCanonicalName()),
    META((byte) 0x03, MetadataSerializer.class.getCanonicalName());

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
