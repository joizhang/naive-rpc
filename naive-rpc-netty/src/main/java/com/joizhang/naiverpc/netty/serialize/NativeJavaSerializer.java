package com.joizhang.naiverpc.netty.serialize;

import com.joizhang.naiverpc.serialize.ObjectInput;
import com.joizhang.naiverpc.serialize.ObjectOutput;
import com.joizhang.naiverpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.joizhang.naiverpc.netty.serialize.SerializerType.NATIVE_JAVA_SERIALIZATION_ID;

@Slf4j
public class NativeJavaSerializer implements Serializer {

    private final static AtomicBoolean warn = new AtomicBoolean(false);

    @Override
    public byte getContentTypeId() {
        return NATIVE_JAVA_SERIALIZATION_ID;
    }

    @Override
    public String getContentType() {
        return "x-application/nativejava";
    }

    @Override
    public ObjectOutput serialize(OutputStream outputStream) throws IOException {
        if (warn.compareAndSet(false, true)) {
            log.warn("Java serialization is unsafe.");
        }
        return new NativeJavaObjectOutput(outputStream);
    }

    @Override
    public ObjectInput deserialize(InputStream inputStream) throws IOException {
        if (warn.compareAndSet(false, true)) {
            log.warn("Java serialization is unsafe.");
        }
        return new NativeJavaObjectInput(inputStream);
    }
}
