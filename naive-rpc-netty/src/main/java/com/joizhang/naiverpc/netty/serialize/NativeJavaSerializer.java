package com.joizhang.naiverpc.netty.serialize;

import com.joizhang.naiverpc.serialize.ObjectInput;
import com.joizhang.naiverpc.serialize.ObjectOutput;
import com.joizhang.naiverpc.serialize.Serializer;
import com.joizhang.naiverpc.spi.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.joizhang.naiverpc.serialize.Constants.NATIVE_JAVA_SERIALIZATION_ID;

public class NativeJavaSerializer implements Serializer {

    private static final Logger logger = LoggerFactory.getLogger(NativeJavaSerializer.class);

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
            logger.warn("Java serialization is unsafe.");
        }
        return new NativeJavaObjectOutput(outputStream);
    }

    @Override
    public ObjectInput deserialize(InputStream inputStream) throws IOException {
        if (warn.compareAndSet(false, true)) {
            logger.warn("Java serialization is unsafe.");
        }
        return new NativeJavaObjectInput(inputStream);
    }
}
