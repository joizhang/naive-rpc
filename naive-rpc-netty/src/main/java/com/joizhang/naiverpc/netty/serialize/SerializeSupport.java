package com.joizhang.naiverpc.netty.serialize;

import com.joizhang.naiverpc.netty.nameservice.Metadata;
import com.joizhang.naiverpc.serialize.ObjectInput;
import com.joizhang.naiverpc.serialize.ObjectOutput;
import com.joizhang.naiverpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@Slf4j
public class SerializeSupport {

    public static <T> byte[] serialize(Serializer serializer, T t) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ObjectOutput serialize = serializer.serialize(byteArrayOutputStream);
            serialize.writeObject(t);
            return byteArrayOutputStream.toByteArray();
        }
    }

    public static <T> T deserialize(Serializer serializer, byte[] bytes, Class<T> clazz)
            throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);){
            ObjectInput deserialize = serializer.deserialize(inputStream);
            return deserialize.readObject(clazz);
        }
    }

}
